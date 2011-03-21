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

public class ReadChunkState extends State<ReadChunkState.ReadChunkStateProcessing> {

	public static enum ReadChunkStateProcessing {
		READ_CHUNK_AGAIN,
		GO_TO_DELIMITER
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		// TODO if preview consider that the string might reach 0; ieof earlier than expected
		IcapChunk chunk = null;
		if(icapMessageDecoder.message.isPreview()) {
			int readable = buffer.readableBytes();
			int currentChunkSize = icapMessageDecoder.currentChunkSize;
			if((readable + IcapCodecUtil.IEOF_SEQUENCE.length) > currentChunkSize) {
				chunk = new DefaultIcapChunk(buffer.readBytes(currentChunkSize));
				chunk.setIsPreviewChunk();
				return StateReturnValue.createRelevantResultWithDecisionInformation(chunk,ReadChunkStateProcessing.GO_TO_DELIMITER);
			} else {
				ChannelBuffer previewBuffer = buffer.readBytes(readable);
				// TODO generalize this and make it static
				byte[] end = new byte[IcapCodecUtil.IEOF_SEQUENCE.length];
				previewBuffer.getBytes(previewBuffer.readableBytes() - IcapCodecUtil.IEOF_SEQUENCE.length,end);
				if(Arrays.equals(IcapCodecUtil.IEOF_SEQUENCE,end)) {
					previewBuffer = previewBuffer.copy(0,previewBuffer.readableBytes() - IcapCodecUtil.IEOF_SEQUENCE.length);
					buffer.readerIndex(buffer.readerIndex() - end.length);
					chunk = new DefaultIcapChunk(previewBuffer);
					chunk.setIsPreviewChunk();
					chunk.setIsEarlyTerminated();
					return StateReturnValue.createRelevantResultWithDecisionInformation(chunk,ReadChunkStateProcessing.READ_CHUNK_AGAIN);
				} else {
					StateReturnValue.createIrrelevantResult();
				}
			}
		}
		
		
		chunk = new DefaultIcapChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
		if(icapMessageDecoder.message.isPreview()) {
			chunk.setIsPreviewChunk();
		}
		return StateReturnValue.createRelevantResultWithDecisionInformation(chunk,ReadChunkStateProcessing.GO_TO_DELIMITER);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, ReadChunkStateProcessing decisionInformation) throws Exception {
		if(decisionInformation.equals(ReadChunkStateProcessing.READ_CHUNK_AGAIN)) {
			return StateEnum.READ_CHUNK_STATE;
		} else if(decisionInformation.equals(ReadChunkStateProcessing.GO_TO_DELIMITER)) {
			return StateEnum.READ_CHUNK_DELIMITER_STATE;
		} else {
			return null;
		}
	}
}
