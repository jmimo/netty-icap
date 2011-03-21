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

import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunk;

public class PreviewState extends State<Object> {
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		int chunkSize = icapMessageDecoder.currentChunkSize;
		
		
		// TODO remove literal
		int previewSize = Integer.parseInt(icapMessageDecoder.message.getHeader("Preview"));
		int readable = buffer.readableBytes();
		ChannelBuffer preview = null;
		
		if(readable > previewSize) {
			preview = buffer.readBytes(previewSize);
		} else {
			preview = buffer.readBytes(readable);
			byte[] end = new byte[IcapCodecUtil.IEOF_SEQUENCE.length];
			preview.getBytes(preview.readableBytes() - IcapCodecUtil.IEOF_SEQUENCE.length,end);
			if(Arrays.equals(IcapCodecUtil.IEOF_SEQUENCE,end)) {
				preview = preview.copy(0,preview.readableBytes() - IcapCodecUtil.IEOF_SEQUENCE.length);
			} else {
				preview = null;
			}
		}
		
		if(preview != null) {
			HttpChunk chunk = new DefaultHttpChunk(buffer.readBytes(previewSize));
			return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
		}
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
//		if(icapMessageDecoder.message.getPreview() == null) {
//			return StateEnum.PREVIEW_STATE;
//		}
		return StateEnum.READ_CHUNK_DELIMITER_STATE;
	}
}
