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
 * Decoder State that reads icap initial.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see StateEnum
 */
public class ReadIcapInitialState extends State<Object> {

	public ReadIcapInitialState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
		// NOOP
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
		String[] initialLine = IcapDecoderUtil.splitInitialLine(IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength));
		icapMessageDecoder.message = icapMessageDecoder.createMessage(initialLine);
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws DecodingException {
		if(icapMessageDecoder.message == null) {
			return StateEnum.SKIP_CONTROL_CHARS;
		}
		return StateEnum.READ_ICAP_HEADER_STATE;
	}
}
