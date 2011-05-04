/*******************************************************************************
 * Copyright (c) 2011 Michael Mimo Moratti.
 *  
 * Michael Mimo Moratti licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
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

/**
 * This ICAP chunk aggregator will combine an received ICAP message with all body chunks.
 * the body is the to be found attached to the correct HTTP request or response instance
 * within the ICAP message.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 * 
 * @see IcapChunkSeparator
 *
 */
public class IcapChunkAggregator extends SimpleChannelUpstreamHandler {

	private static final InternalLogger LOG = InternalLoggerFactory.getInstance(IcapChunkAggregator.class);
	
	private long maxContentLength;
	private IcapMessageWrapper message;
	
	/**
	 * Convenience method to retrieve a HTTP request,response or 
	 * an ICAP options response body from an aggregated IcapMessage. 
	 * @param message
	 * @return null or @see {@link ChannelBuffer} if a body exists.
	 */
	public static ChannelBuffer extractHttpBodyContentFromIcapMessage(IcapMessage message) {
		ChannelBuffer buffer = null;
		if(message.getBody().equals(IcapMessageElementEnum.REQBODY) && message.getHttpRequest() != null) {
			buffer = message.getHttpRequest().getContent();
		} else if(message.getBody().equals(IcapMessageElementEnum.RESBODY) && message.getHttpResponse() != null) {
			buffer = message.getHttpResponse().getContent();
		} else if(message instanceof IcapResponse && message.getBody().equals(IcapMessageElementEnum.OPTBODY)) {
			IcapResponse response = (IcapResponse)message;
			buffer = response.getContent();
		}
		return buffer;
	}
	
	/**
	 * @param maxContentLength defines the maximum length of the body content that is allowed. 
	 * If the length is exceeded an exception is thrown.
	 */
	public IcapChunkAggregator(long maxContentLength) {
		this.maxContentLength = maxContentLength;
	}
	
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	Object msg = e.getMessage();
    	if(msg instanceof IcapMessage) {
    		LOG.debug("Aggregation of message [" + msg.getClass().getName() + "] ");
    		IcapMessage currentMessage = (IcapMessage)msg;
    		message = new IcapMessageWrapper(currentMessage);
    		if(!message.hasBody()) {
    			Channels.fireMessageReceived(ctx,message.getIcapMessage(),e.getRemoteAddress());
    			message = null;
    			return;
    		}
    	} else if(msg instanceof IcapChunkTrailer) {
    		LOG.debug("Aggregation of chunk trailer [" + msg.getClass().getName() + "] ");
    		if(message == null) {
    			ctx.sendUpstream(e);
    		} else {
    			IcapChunkTrailer trailer = (IcapChunkTrailer)msg;
    			if(trailer.getHeaderNames().size() > 0) {		
    				for(String name : trailer.getHeaderNames()) {
    					message.addHeader(name,trailer.getHeader(name));
    				}
    			}
    			Channels.fireMessageReceived(ctx,message.getIcapMessage(),e.getRemoteAddress());
    		}
    	} else if(msg instanceof IcapChunk) {
    		LOG.debug("Aggregation of chunk [" + msg.getClass().getName() + "] ");
    		IcapChunk chunk = (IcapChunk)msg;
    		if(message == null) {
    			ctx.sendUpstream(e);
    		} else if(chunk.isLast()) {
    			Channels.fireMessageReceived(ctx,message.getIcapMessage(),e.getRemoteAddress());
    			message = null;
    		} else {
	    		ChannelBuffer chunkBuffer = chunk.getContent();
	    		ChannelBuffer content = message.getContent();
    			if(content.readableBytes() > maxContentLength - chunkBuffer.readableBytes()) {
    				throw new TooLongFrameException("ICAP content length exceeded [" + maxContentLength + "] bytes");
    			} else {
    				content.writeBytes(chunkBuffer);
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
    	
    	public IcapMessageWrapper(IcapMessage message) {
    		this.message = message;
    		if(message.getBody() != null) {
	    		if(message.getBody().equals(IcapMessageElementEnum.REQBODY)) {
	    			relevantHttpMessage = message.getHttpRequest();
	    			messageWithBody = true;
	    		} else if(message.getBody().equals(IcapMessageElementEnum.RESBODY)) {
	    			relevantHttpMessage = message.getHttpResponse();
	    			messageWithBody = true;
	    		} else if(message instanceof IcapResponse && message.getBody().equals(IcapMessageElementEnum.OPTBODY)) {
	    			icapResponse = (IcapResponse)message;
	    			messageWithBody = true;
	    		}
    		}
    		if(messageWithBody) {
    			if(relevantHttpMessage != null) {
	    			if(relevantHttpMessage.getContent() == null || relevantHttpMessage.getContent().readableBytes() <= 0) {
	    				relevantHttpMessage.setContent(ChannelBuffers.dynamicBuffer());
	    			}
    			} else if(icapResponse != null) {
    				if(icapResponse.getContent() == null || icapResponse.getContent().readableBytes() <= 0) {
    					icapResponse.setContent(ChannelBuffers.dynamicBuffer());
    				}
    			}
    		}
    	}
    	
    	public boolean hasBody() {
    		return messageWithBody;
    	}
    	
    	public IcapMessage getIcapMessage() {
    		return message;
    	}
    	
    	public void addHeader(String name, String value) {
    		if(messageWithBody) {
    			relevantHttpMessage.addHeader(name,value);
    		} else {
    			throw new IcapDecodingError("A message without body cannot carry trailing headers.");
    		}
    	}
    	
    	public ChannelBuffer getContent() {
    		if(messageWithBody) {
    			if(relevantHttpMessage != null) {
    				return relevantHttpMessage.getContent();
    			} else if(icapResponse != null) {
    				return icapResponse.getContent();
    			}
    		}
    		throw new IcapDecodingError("Message stated that there is a body but nothing found in message.");
    	}
    }
}
