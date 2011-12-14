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
package ch.mimo.netty.example.icap.preview;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import ch.mimo.netty.handler.codec.icap.DefaultIcapChunk;
import ch.mimo.netty.handler.codec.icap.DefaultIcapChunkTrailer;
import ch.mimo.netty.handler.codec.icap.IcapChunk;
import ch.mimo.netty.handler.codec.icap.IcapChunkTrailer;
import ch.mimo.netty.handler.codec.icap.IcapResponse;
import ch.mimo.netty.handler.codec.icap.IcapResponseStatus;

public class IcapClientHandler extends SimpleChannelUpstreamHandler {
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		IcapResponse response = (IcapResponse)e.getMessage();
		if(response.getStatus().equals(IcapResponseStatus.CONTINUE)) {
			System.out.println(response.toString());
	        IcapChunk chunk = new DefaultIcapChunk(ChannelBuffers.copiedBuffer("ns why and how we can avoid such a desaster next time...".getBytes()));
	        IcapChunkTrailer trailer = new DefaultIcapChunkTrailer(true,false);
	        ctx.getChannel().write(chunk);
	        ctx.getChannel().write(trailer);
		} else if(response.getStatus().equals(IcapResponseStatus.NO_CONTENT)) {
			System.out.println(response.toString());
		}
	}
}
