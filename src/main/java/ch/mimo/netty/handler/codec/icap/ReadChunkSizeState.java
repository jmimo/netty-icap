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

public class ReadChunkSizeState extends State<ReadChunkSizeState.DecisionState> {

	public static enum DecisionState {
		READ_CHUNK,
		READ_HUGE_CHUNK_IN_SMALER_CHUNKS,
		READ_TRAILING_HEADERS,
		IS_PREVIEW,
		IS_LAST_CHUNK
	}
	
	public ReadChunkSizeState(String name) {
		super(name);
	}
	
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
			return StateReturnValue.createRelevantResultWithDecisionInformation(new DefaultIcapChunkTrailer(true,true),DecisionState.IS_PREVIEW);
		} else if(chunkSize == 0) { 
			byte previewByte = buffer.getByte(buffer.readerIndex() + 1);
			if(previewByte == IcapCodecUtil.CR | previewByte == IcapCodecUtil.LF) {
				return StateReturnValue.createRelevantResultWithDecisionInformation(new DefaultIcapChunkTrailer(icapMessageDecoder.message.isPreviewMessage(),false),DecisionState.IS_LAST_CHUNK);
			} else {
				return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.READ_TRAILING_HEADERS);
			}
		} else {
			icapMessageDecoder.currentChunkSize = chunkSize;
			if(chunkSize >= icapMessageDecoder.maxChunkSize) {
				return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.READ_HUGE_CHUNK_IN_SMALER_CHUNKS);
			} else {
				return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.READ_CHUNK);
			}
		}
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, DecisionState decisionInformation) throws Exception {
		StateEnum returnValue = null;
		switch (decisionInformation) {
		case READ_CHUNK:
			returnValue = StateEnum.READ_CHUNK_STATE;
			break;
		case READ_HUGE_CHUNK_IN_SMALER_CHUNKS:
			returnValue = StateEnum.READ_CHUNKED_CONTENT_AS_CHUNKS_STATE;
			break;
		case READ_TRAILING_HEADERS:
			returnValue = StateEnum.READ_TRAILING_HEADERS_STATE;
			break;
		case IS_LAST_CHUNK:
			// NOOP
			break;
		case IS_PREVIEW:
			// NOOP
			break;
		default:
			// NOOP
			break;
		}
		return returnValue;
	}
}
