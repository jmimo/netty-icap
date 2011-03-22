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
import java.util.Stack;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.internal.StringUtil;

public class ReadChunkState extends State<ReadChunkState.ReadChunkStateProcessing> {

	public static enum ReadChunkStateProcessing {
		READ_CHUNK_AGAIN,
		GO_TO_DELIMITER,
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		IcapChunk chunk = null;
		if(icapMessageDecoder.message.isPreview()) {
			Matcher ieofMatcher = new Matcher(IcapCodecUtil.IEOF_SEQUENCE);
			ChannelBuffer previewBuffer = ChannelBuffers.dynamicBuffer();
			int counter = 0;
			while(counter < icapMessageDecoder.currentChunkSize) {
				Byte bite = null;
				try {
					bite = buffer.readByte();
					counter++;
				} catch(IndexOutOfBoundsException ioobe) {
					return StateReturnValue.createIrrelevantResultWithDecisionInformation(ReadChunkStateProcessing.READ_CHUNK_AGAIN);
				}
				previewBuffer.writeByte(bite);
				if(ieofMatcher.addByteAndMatch(bite)) {
					ChannelBuffer ieofBuffer = removeSequenceFromEndOfBuffer(previewBuffer,IcapCodecUtil.IEOF_SEQUENCE);
					buffer.readerIndex(buffer.readerIndex() - IcapCodecUtil.IEOF_SEQUENCE.length - StringUtil.NEWLINE.getBytes().length);
					chunk = new DefaultIcapChunk(ieofBuffer);
					chunk.setIsPreviewChunk();
					chunk.setIsEarlyTerminated();
					icapMessageDecoder.currentChunkSize = 0;
					return StateReturnValue.createRelevantResultWithDecisionInformation(chunk,ReadChunkStateProcessing.GO_TO_DELIMITER);
				} 
			}
			chunk = new DefaultIcapChunk(previewBuffer);
			chunk.setIsPreviewChunk();
			return StateReturnValue.createRelevantResultWithDecisionInformation(chunk,ReadChunkStateProcessing.GO_TO_DELIMITER);
		} else {
			chunk = new DefaultIcapChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
			return StateReturnValue.createRelevantResultWithDecisionInformation(chunk,ReadChunkStateProcessing.GO_TO_DELIMITER);
		}
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
	
	private ChannelBuffer removeSequenceFromEndOfBuffer(ChannelBuffer buffer, Byte[] sequence) {
		return buffer.copy(0,buffer.writerIndex() - sequence.length);
	}
	
	private class Matcher {
		
		private Byte[] pattern;
		private int length;
		private Stack<Byte> window;
		
		
		public Matcher(Byte[] pattern) {
			this.pattern = pattern;
			length = pattern.length;
			this.window = new Stack<Byte>();
		}
		
		private boolean addByteAndMatch(byte bite) {
			if(window.size() >= length) {
				window.remove(0);
			}
			window.add(bite);
			Byte[] array = window.toArray(new Byte[0]);
			boolean result = Arrays.equals(pattern,array);
//			System.out.println("==============================");
//			System.out.print("Matcher: [");
//			for(Byte bitex : pattern) {
//				String car = new String(new byte[]{bitex});
//				if(bitex == 13) {
//					System.out.print("cr");
//				} else if(bitex == 10) {
//					System.out.print("lf");
//				} else {
//					System.out.print(car);
//				}
//			}
//			System.out.print("]");
//			
//			System.out.print(" equals [");
//			for(Byte bite1 : array) {
//				String car = new String(new byte[]{bite1});
//				if(bite1 == 13) {
//					System.out.print("cr");
//				} else if(bite1 == 10) {
//					System.out.print("lf");
//				} else {
//					System.out.print(car);
//				}
//			}
//			System.out.println("] result = [" + result + "]");
//			System.out.println("==============================");
			return result;
		}
		
	}
}
