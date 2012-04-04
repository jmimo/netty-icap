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

/**
 * Encodes an ICAP Request which takes an @see {@link IcapRequest} or @see {@link IcapChunk} to encode.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class IcapRequestEncoder extends IcapMessageEncoder {

	public IcapRequestEncoder() {
		super();
	}
	
	@Override
	protected int encodeInitialLine(ChannelBuffer buffer, IcapMessage message) throws Exception {
		IcapRequest request = (IcapRequest) message;
		int index = buffer.readableBytes();
        buffer.writeBytes(request.getMethod().toString().getBytes(IcapCodecUtil.ASCII_CHARSET));
        buffer.writeByte(IcapCodecUtil.SPACE);
        buffer.writeBytes(request.getUri().getBytes(IcapCodecUtil.ASCII_CHARSET));
        buffer.writeByte(IcapCodecUtil.SPACE);
        buffer.writeBytes(request.getProtocolVersion().toString().getBytes(IcapCodecUtil.ASCII_CHARSET));
        buffer.writeBytes(IcapCodecUtil.CRLF);
        return buffer.readableBytes() - index;
	}

}
