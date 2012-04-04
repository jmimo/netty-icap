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
 * Decoder State that reads chunk content
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see StateEnum
 */
public class ReadChunkState extends State<Object> {
	
	public ReadChunkState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {		
		IcapChunk chunk = new DefaultIcapChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
		chunk.setPreviewChunk(icapMessageDecoder.message.isPreviewMessage());
		return StateReturnValue.createRelevantResult(chunk);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws DecodingException {
		return StateEnum.READ_CHUNK_DELIMITER_STATE;
	}	
}
