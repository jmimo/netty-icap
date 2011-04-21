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
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class IcapChunkSeparator extends SimpleChannelUpstreamHandler {

	private int chunkSize;
	
	public IcapChunkSeparator(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	Object msg = e.getMessage();
    	if(msg instanceof IcapMessage) {
    		IcapMessage message = (IcapMessage)msg;
    		Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    		ChannelBuffer content = null;
    		if(message.getHttpRequest() != null && message.getHttpRequest().getContent() != null && message.getHttpRequest().getContent().readableBytes() > 0) {
    			content = message.getHttpRequest().getContent();
    			message.setBody(IcapMessageElementEnum.REQBODY);
    		} else if(message.getHttpResponse() != null && message.getHttpResponse().getContent() != null && message.getHttpResponse().getContent().readableBytes() > 0) {
    			content = message.getHttpResponse().getContent();
    			message.setBody(IcapMessageElementEnum.RESBODY);
    		}
    		// TODO implement options body for this we need to introduce a ChannelBuffer member in the IcapMessage implementation.
    		if(content != null) {
    			boolean isPreview = message.isPreviewMessage();
    			boolean isEarlyTerminated = false;
    			if(isPreview) {
    				int amount = 0;
    				try {
    					amount = Integer.parseInt(message.getHeader(IcapHeaders.Names.PREVIEW));
    				} catch(NumberFormatException nfe) {
    					// NOOP
    				}
    				isEarlyTerminated = content.readableBytes() < amount;
    			}
    			boolean dataWasSent = false;
				while(content.readableBytes() > 0) {
					IcapChunk chunk = null;
					if(content.readableBytes() > chunkSize) {
						chunk = new DefaultIcapChunk(content.readBytes(chunkSize));
					} else {
						chunk = new DefaultIcapChunk(content.readBytes(content.readableBytes()));
					}
					chunk.setPreviewChunk(isPreview);
					chunk.setEarlyTermination(isEarlyTerminated);
					Channels.fireMessageReceived(ctx,chunk,e.getRemoteAddress());
					dataWasSent = true;
				}
				if(dataWasSent) {
					IcapChunkTrailer trailer = new DefaultIcapChunkTrailer();
					trailer.setPreviewChunk(isPreview);
					trailer.setEarlyTermination(isEarlyTerminated);
					// TODO we are currently unable to handle trailing headers. for this we have to specify in the message that there are 
					// trailing headers and what they are named.
					Channels.fireMessageReceived(ctx,trailer,e.getRemoteAddress());
				}
    		}
    	} else {
    		ctx.sendUpstream(e);
    	}
    }
}
