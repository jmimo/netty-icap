/*******************************************************************************
 * Copyright (c) 2012 Michael Mimo Moratti.
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

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;

/**
 * A combination of @see {@link IcapRequestDecoder} and @see {@link IcapResponseEncoder}
 * which enables easier server side ICAP implementation.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapRequestDecoder
 * @see IcapResponseEncoder
 * @see IcapClientCodec
 */
public class IcapServerCodec implements ChannelUpstreamHandler, ChannelDownstreamHandler {

	private final IcapRequestDecoder decoder;
	private final IcapResponseEncoder encoder = new IcapResponseEncoder();
	
	public IcapServerCodec() {
		this(4096,8192,8192,8192);
	}
	
	/**
	 * @see IcapMessageDecoder IcapMessageDecoder constructor for more details.
	 * 
	 * @param maxInitialLineLength
	 * @param maxIcapHeaderSize
	 * @param maxHttpHeaderSize
	 * @param maxChunkSize
	 */
	public IcapServerCodec(int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
		decoder = new IcapRequestDecoder(maxInitialLineLength,maxIcapHeaderSize,maxHttpHeaderSize,maxChunkSize);
	}
	
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		encoder.handleDownstream(ctx, e);
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		decoder.handleUpstream(ctx, e);
	}

}
