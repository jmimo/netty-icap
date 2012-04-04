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

/**
 * Decoder State that reads chunk size
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see StateEnum
 */
public class ReadChunkSizeState extends State<ReadChunkSizeState.DecisionState> {

	/**
	 * Used to decide what state has to be processed next.
	 * 
	 * @author Michael Mimo Moratti (mimo@mimo.ch)
	 *
	 */
	public static enum DecisionState {
		READ_CHUNK(StateEnum.READ_CHUNK_STATE),
		READ_HUGE_CHUNK_IN_SMALER_CHUNKS(StateEnum.READ_CHUNKED_CONTENT_AS_CHUNKS_STATE),
		READ_TRAILING_HEADERS(StateEnum.READ_TRAILING_HEADERS_STATE),
		IS_LAST_PREVIEW_CHUNK(StateEnum.READ_CHUNK_SIZE_STATE),
		IS_LAST_CHUNK(StateEnum.SKIP_CONTROL_CHARS),
		RESET(null);
		
		private StateEnum state;
		
		DecisionState(StateEnum nextState) {
			state = nextState;
		}
		
		public StateEnum getNextState() {
			return state;
		}
	}
	
	public ReadChunkSizeState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
	}

	@Override
	/**
	 * 1. chunk size is not 0 --> normal chunk reading is required. (go to chunk reading)
	 * 2. chunk size is 0 --> end of body (end of processing)
	 * 3. chunk size is 0 and we are in preview mode / preview is therefore sent and possible more is to come. (stay in state)
	 * 4. chunk size is 0; ieof early end of preview (stay in state)
	 * 5. next message arrives and we are still in preview mode. (step out and start over)
	 *
	 * Strategy:
	 * 
	 * 1. preview read next line
	 * 2. attempt to parse chunk length
	 * 2a. chunk length could not be parsed step out (END STATE/reset).
	 * 2b. chunk length could be parsed, adjust readerIndex by reading the line.
	 * 3. chunk size > 0, read the next chunk. (END STATE).
	 * 4. chunk size == -1, early termination of preview reading process. Stay in state and wait for more data.
	 * 5. chunk size == 0 and message is preview message. Stay in state and wait for more data.
	 * 6. chunk size == 0 step out (END STATE/reset).
	 */
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
		int chunkSize = 0;
		String previewLine = IcapDecoderUtil.previewLine(buffer,icapMessageDecoder.maxInitialLineLength);
		try {
			chunkSize = IcapDecoderUtil.getChunkSize(previewLine);
			IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength);
		} catch(DecodingException de) {
			return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.RESET);
		}
		icapMessageDecoder.currentChunkSize = chunkSize;
		if(chunkSize > 0) {
			if(chunkSize >= icapMessageDecoder.maxChunkSize) {
				return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.READ_HUGE_CHUNK_IN_SMALER_CHUNKS);
			} else {
				return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.READ_CHUNK);
			}
		} else if(chunkSize == -1) {
			icapMessageDecoder.currentChunkSize = 0;
			IcapDecoderUtil.readLine(buffer,Integer.MAX_VALUE);
			return StateReturnValue.createRelevantResultWithDecisionInformation(new DefaultIcapChunkTrailer(true,true),DecisionState.IS_LAST_PREVIEW_CHUNK);
		} else if(chunkSize == 0) {
			if(!checkForLineBreak(buffer)) {
				return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.READ_TRAILING_HEADERS);
			}
			IcapDecoderUtil.readLine(buffer,10);
			if(icapMessageDecoder.message.isPreviewMessage()) {
				return StateReturnValue.createRelevantResultWithDecisionInformation(new DefaultIcapChunkTrailer(true,false),DecisionState.IS_LAST_PREVIEW_CHUNK);
			} else {
				return StateReturnValue.createRelevantResultWithDecisionInformation(new DefaultIcapChunkTrailer(icapMessageDecoder.message.isPreviewMessage(),false),DecisionState.IS_LAST_CHUNK);
			}		
		} else {
			return StateReturnValue.createIrrelevantResultWithDecisionInformation(DecisionState.RESET);
		}
	}
	
	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, DecisionState decisionInformation) throws DecodingException {
		return decisionInformation.getNextState();
	}
	
	private boolean checkForLineBreak(ChannelBuffer buffer) {
		byte previewByte = buffer.getByte(buffer.readerIndex() + 1);
		return previewByte == IcapCodecUtil.CR | previewByte == IcapCodecUtil.LF;
	}
}
