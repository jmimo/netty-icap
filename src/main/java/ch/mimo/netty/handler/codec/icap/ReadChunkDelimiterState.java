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
 * Decoder State that reads chunk delimiters
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see StateEnum
 */
public class ReadChunkDelimiterState extends State<Object> {

	public ReadChunkDelimiterState(String name) {
		super(name);
	}

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
        for (;;) {
            byte next = buffer.readByte();
            if (next == IcapCodecUtil.CR) {
                if (buffer.readByte() == IcapCodecUtil.LF) {
                    return StateReturnValue.createIrrelevantResult();
                }
            } else if (next == IcapCodecUtil.LF) {
                return StateReturnValue.createIrrelevantResult();
            }
        }
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws DecodingException {
		return StateEnum.READ_CHUNK_SIZE_STATE;
	}

}
