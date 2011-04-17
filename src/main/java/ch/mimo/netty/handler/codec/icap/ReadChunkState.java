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


public class ReadChunkState extends State<Object> {
	
	public ReadChunkState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {		
		IcapChunk chunk = new DefaultIcapChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
		if(icapMessageDecoder.message.isPreviewMessage()) {
			chunk.setIsPreviewChunk();
		}
		return StateReturnValue.createRelevantResult(chunk);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		return StateEnum.READ_CHUNK_DELIMITER_STATE;
	}	
}
