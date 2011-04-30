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

/**
 * Decoder State that reads one huge chunk into many smaller chunks
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see StateEnum
 */
public class ReadChunkedContentAsChunksState extends State<Object> {

	public ReadChunkedContentAsChunksState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		IcapChunk chunk = null;
		if(icapMessageDecoder.currentChunkSize > icapMessageDecoder.maxChunkSize) {
			chunk = new DefaultIcapChunk(buffer.readBytes(icapMessageDecoder.maxChunkSize));
			icapMessageDecoder.currentChunkSize -= icapMessageDecoder.maxChunkSize;
		} else {
			chunk = new DefaultIcapChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
			icapMessageDecoder.currentChunkSize = 0;
		}
		chunk.setPreviewChunk(icapMessageDecoder.message.isPreviewMessage());
		if(chunk.isLast()) {
			icapMessageDecoder.currentChunkSize = 0;
			return StateReturnValue.createRelevantResult(new Object[]{chunk,new DefaultIcapChunkTrailer()}); 
		}
		return StateReturnValue.createRelevantResult(chunk);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		if(icapMessageDecoder.currentChunkSize == 0) {
			return StateEnum.READ_CHUNK_DELIMITER_STATE;
		}
		return StateEnum.READ_CHUNKED_CONTENT_AS_CHUNKS_STATE;
	}

}
