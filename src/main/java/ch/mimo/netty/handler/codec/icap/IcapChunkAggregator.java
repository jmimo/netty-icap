/*******************************************************************************
 * Copyright 2012 Michael Mimo Moratti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

import java.net.SocketAddress;

/**
 * This ICAP chunk aggregator will combine an received ICAP message with all body chunks.
 * the body is the to be found attached to the correct HTTP request or response instance
 * within the ICAP message.
 * <p/>
 * In case when a Preview IcapRequest is received with an early chunk termination the preview indication
 * and header are removed entirely from the message. This is done because a preview message with an early
 * content termination is in essence nothing else than a full message.
 * <p/>
 * The reader index of an HTTP content ChannelBuffer can be reset to 0 via a dedicated constructor in order to handle preview aggregation.
 * This is done in order to allow server implementations to handle preview messages properly. A preview message
 * is aggregated with the 100 Continue response from the client and the buffer will be therefore reset to 0
 * so that the server handler can read the entire message.
 *
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 * @see IcapChunkSeparator
 */
public class IcapChunkAggregator extends SimpleChannelUpstreamHandler {

    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(IcapChunkAggregator.class);

    private static final int READER_INDEX_RESET_VALUE = 0;

    private long maxContentLength;
    private IcapMessageWrapper message;
    private boolean resetReaderIndex;

    /**
     * Convenience method to retrieve a HTTP request,response or
     * an ICAP options response body from an aggregated IcapMessage.
     *
     * @param message
     * @return null or @see {@link ChannelBuffer} if a body exists.
     */
    public static ChannelBuffer extractHttpBodyContentFromIcapMessage(IcapMessage message) {
        ChannelBuffer buffer = null;
        if (message != null) {
            if (message.getHttpRequest() != null && message.getHttpRequest().getContent().readableBytes() > 0) {
                buffer = message.getHttpRequest().getContent();
            } else if (message.getHttpResponse() != null && message.getHttpResponse().getContent().readableBytes() > 0) {
                buffer = message.getHttpResponse().getContent();
            } else if (message instanceof IcapResponse) {
                if (((IcapResponse) message).getContent().readableBytes() > 0) {
                    buffer = ((IcapResponse) message).getContent();
                }
            }
        }
        return buffer;
    }

    /**
     * @param maxContentLength defines the maximum length of the body content that is allowed.
     *                         If the length is exceeded an exception is thrown.
     */
    public IcapChunkAggregator(long maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    /**
     * Constructor that allows to change the preview body update behavior to
     * reset the HTTP message body channel reader index when receiving the rest
     * of the body after a 100 Continue.
     *
     * @param maxContentLength defines the maximum length of the body content that is allowed.
     * @param resetReaderIndex defines if the HTTP message reader index should be reset after adding more data to it.
     */
    public IcapChunkAggregator(long maxContentLength, boolean resetReaderIndex) {
        this(maxContentLength);
        this.resetReaderIndex = resetReaderIndex;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (msg instanceof IcapMessage) {
            LOG.debug("Aggregation of message [" + msg.getClass().getName() + "] ");
            IcapMessage currentMessage = (IcapMessage) msg;
            message = new IcapMessageWrapper(currentMessage);
            if (!message.hasBody()) {
                LOG.debug("message has no body, removing PREVIEW header");
                message.clearPreview();
                message.sendToHandler(ctx,e.getRemoteAddress());
                message = null;
                return;
            }
        } else if (msg instanceof IcapChunkTrailer) {
            LOG.debug("Aggregation of chunk trailer [" + msg.getClass().getName() + "] ");
            if (message == null) {
                ctx.sendUpstream(e);
            } else {
                IcapChunkTrailer trailer = (IcapChunkTrailer) msg;
                if (trailer.isEarlyTerminated()) {
                    LOG.debug("chunk trailer is early terminated, removing PREVIEW header");
                    message.clearPreview();
                }
                if (trailer.getHeaderNames().size() > 0) {
                    for (String name : trailer.getHeaderNames()) {
                        message.addHeader(name, trailer.getHeader(name));
                    }
                }

                message.sendToHandler(ctx, e.getRemoteAddress());
            }
        } else if (msg instanceof IcapChunk) {
            LOG.debug("Aggregation of chunk [" + msg.getClass().getName() + "] ");
            IcapChunk chunk = (IcapChunk) msg;
            if (message == null) {
                ctx.sendUpstream(e);
            } else if (chunk.isLast()) {
                if (chunk.isEarlyTerminated()) {
                    LOG.debug("chunk is early terminated, removing PREVIEW header");
                    message.clearPreview();
                }
                message.sendToHandler(ctx, e.getRemoteAddress());
                message = null;
            } else {
                ChannelBuffer chunkBuffer = chunk.getContent();
                ChannelBuffer content = message.getContent();
                if (content.readableBytes() > maxContentLength - chunkBuffer.readableBytes()) {
                    throw new TooLongFrameException("ICAP content length exceeded [" + maxContentLength + "] bytes");
                } else {
                    content.writeBytes(chunkBuffer);
                    if (resetReaderIndex) {
                        content.readerIndex(READER_INDEX_RESET_VALUE);
                    }
                }
            }
        } else {
            ctx.sendUpstream(e);
        }
    }

    private final class IcapMessageWrapper {

        private IcapMessage message;
        private HttpMessage relevantHttpMessage;
        private IcapResponse icapResponse;
        private boolean messageWithBody;
        private boolean previewMessage;

        public IcapMessageWrapper(IcapMessage message) {
            this.message = message;
            if (message.getBodyType() != null) {
                if (message.getBodyType().equals(IcapMessageElementEnum.REQBODY)) {
                    relevantHttpMessage = message.getHttpRequest();
                    messageWithBody = true;
                } else if (message.getBodyType().equals(IcapMessageElementEnum.RESBODY)) {
                    relevantHttpMessage = message.getHttpResponse();
                    messageWithBody = true;
                } else if (message instanceof IcapResponse && message.getBodyType().equals(IcapMessageElementEnum.OPTBODY)) {
                    icapResponse = (IcapResponse) message;
                    messageWithBody = true;
                }
            }
            if (messageWithBody) {
                if (relevantHttpMessage != null) {
                    if (relevantHttpMessage.getContent() == null || relevantHttpMessage.getContent().readableBytes() <= 0) {
                        relevantHttpMessage.setContent(ChannelBuffers.dynamicBuffer());
                    }
                } else if (icapResponse != null) {
                    if (icapResponse.getContent() == null || icapResponse.getContent().readableBytes() <= 0) {
                        icapResponse.setContent(ChannelBuffers.dynamicBuffer());
                    }
                }
            }
            previewMessage =true;
        }

        public void sendToHandler(ChannelHandlerContext ctx, SocketAddress remoteAddress) {
            if (!previewMessage) message.removeHeader(IcapHeaders.Names.PREVIEW);
            Channels.fireMessageReceived(ctx, message, remoteAddress);
            previewMessage = false;
        }

        public void clearPreview() {
            message.removeHeader(IcapHeaders.Names.PREVIEW);
            previewMessage = false;
        }

        public boolean hasBody() {
            return messageWithBody;
        }

        public IcapMessage getIcapMessage() {
            return message;
        }

        public void addHeader(String name, String value) {
            if (messageWithBody) {
                relevantHttpMessage.addHeader(name, value);
            } else {
                throw new IcapDecodingError("A message without body cannot carry trailing headers.");
            }
        }

        public ChannelBuffer getContent() {
            if (messageWithBody) {
                if (relevantHttpMessage != null) {
                    return relevantHttpMessage.getContent();
                } else if (icapResponse != null) {
                    return icapResponse.getContent();
                }
            }
            throw new IcapDecodingError("Message stated that there is a body but nothing found in message.");
        }
    }
}
