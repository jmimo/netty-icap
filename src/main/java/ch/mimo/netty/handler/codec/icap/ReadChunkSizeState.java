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

public class ReadChunkSizeState extends State<Integer> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		String line = IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength);
		int chunkSize = IcapDecoderUtil.getChunkSize(line);
		icapMessageDecoder.currentChunkSize = chunkSize;
		if(chunkSize == -1) {
			icapMessageDecoder.currentChunkSize = 0;
			return StateReturnValue.createRelevantResultWithDecisionInformation(new DefaultIcapChunkTrailer(true,true),0);
		} else {
			icapMessageDecoder.currentChunkSize = chunkSize;
			return StateReturnValue.createIrrelevantResultWithDecisionInformation(chunkSize);
		}
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Integer decisionInformation) throws Exception {
		if(decisionInformation >= icapMessageDecoder.maxChunkSize) {
			return StateEnum.READ_CHUNKED_CONTENT_AS_CHUNKS_STATE;
		}
		if(decisionInformation == 0) {
			return StateEnum.READ_TRAILING_HEADERS_STATE;
		}
		return StateEnum.READ_CHUNK_STATE;
	}

}
