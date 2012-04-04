/*******************************************************************************
 * Copyright 2011 - 2012 Michael Mimo Moratti
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
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

/**
 * Separates a received ICAP message and body that is attached to either the HTTP request or response.
 * 
 * In other words. This handler allows to create a combined ICAP message containing HTTP request/response and
 * the corresponding body as ChannelBuffer include in one of the HTTP relevant instances.
 * 
 * This separator cannot handle trailing headers at HTTP request or response bodies. If you have to
 * send trailing headers then consider not using this separator but handling the message body by yourself.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class IcapChunkSeparator implements ChannelDownstreamHandler {

	private static final InternalLogger LOG = InternalLoggerFactory.getInstance(IcapChunkSeparator.class);
	
	private int chunkSize;
	
	/**
	 * @param chunkSize defines the normal chunk size that is to be produced while separating.
	 */
	public IcapChunkSeparator(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof MessageEvent) {
			MessageEvent msgEvent = (MessageEvent)e;
			Object msg = msgEvent.getMessage();
	    	if(msg instanceof IcapMessage) {
	    		LOG.debug("Separation of message [" + msg.getClass().getName() + "] ");
	    		IcapMessage message = (IcapMessage)msg;
	    		ChannelBuffer content = extractContentFromMessage(message);
	    		fireDownstreamEvent(ctx,message,msgEvent);
	    		if(content != null) {
	    			boolean isPreview = message.isPreviewMessage();
	    			boolean isEarlyTerminated = false;
	    			if(isPreview) {
	    				isEarlyTerminated = content.readableBytes() < message.getPreviewAmount();
	    			}
					while(content.readableBytes() > 0) {
						IcapChunk chunk = null;
						if(content.readableBytes() > chunkSize) {
							chunk = new DefaultIcapChunk(content.readBytes(chunkSize));
						} else {
							chunk = new DefaultIcapChunk(content.readBytes(content.readableBytes()));
						}
						chunk.setPreviewChunk(isPreview);
						chunk.setEarlyTermination(isEarlyTerminated);
						fireDownstreamEvent(ctx,chunk,msgEvent);
						if(chunk.isLast() | content.readableBytes() <= 0) {
							IcapChunkTrailer trailer = new DefaultIcapChunkTrailer();
							trailer.setPreviewChunk(isPreview);
							trailer.setEarlyTermination(isEarlyTerminated);
							fireDownstreamEvent(ctx,trailer,msgEvent);
						}
					}
	    		}
	    	} else {
	    		ctx.sendDownstream(e);
	    	}
		} else {
			ctx.sendDownstream(e);
		}
	}
    
	private ChannelBuffer extractContentFromMessage(IcapMessage message) {
		ChannelBuffer content = null;
		if(message instanceof IcapResponse && ((IcapResponse)message).getContent() != null) {
			IcapResponse response = (IcapResponse)message;
			content = response.getContent();
			if(content != null) {
				message.setBody(IcapMessageElementEnum.OPTBODY);
			}
		} else if(message.getHttpRequest() != null && message.getHttpRequest().getContent() != null && message.getHttpRequest().getContent().readableBytes() > 0) {
			content = message.getHttpRequest().getContent();
			message.setBody(IcapMessageElementEnum.REQBODY);
		} else if(message.getHttpResponse() != null && message.getHttpResponse().getContent() != null && message.getHttpResponse().getContent().readableBytes() > 0) {
			content = message.getHttpResponse().getContent();
			message.setBody(IcapMessageElementEnum.RESBODY);
		}
		return content;
	}
	
    private void fireDownstreamEvent(ChannelHandlerContext ctx, Object message, MessageEvent messageEvent) {
    	DownstreamMessageEvent downstreamMessageEvent = 
    		new DownstreamMessageEvent(ctx.getChannel(),messageEvent.getFuture(),message,messageEvent.getRemoteAddress());
    	ctx.sendDownstream(downstreamMessageEvent);
    }
}
