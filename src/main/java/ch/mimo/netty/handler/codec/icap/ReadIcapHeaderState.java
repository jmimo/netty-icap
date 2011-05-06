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

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Decoder State that reads icap headers.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see StateEnum
 */
public class ReadIcapHeaderState extends State<Object> {
	
	public ReadIcapHeaderState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
		List<String[]> headerList = IcapDecoderUtil.readHeaders(buffer,icapMessageDecoder.maxIcapHeaderSize);
		icapMessageDecoder.message.clearHeaders();
		for(String[] header : headerList) {
			icapMessageDecoder.message.addHeader(header[0],header[1]);
		}
		validateMandatoryMessageHeaders(icapMessageDecoder.message,icapMessageDecoder.isDecodingResponse());
		Encapsulated encapsulated = new Encapsulated(icapMessageDecoder.message.getHeader(IcapHeader.Names.ENCAPSULATED));
		icapMessageDecoder.message.setEncapsulatedHeader(encapsulated);
		// TODO find less complex and more generic solution. This statement assumes that there is a res hds always after the reqhdr.
		// this by the way is correct when assuming the other end is adhering 100% to the rfc!
		if(icapMessageDecoder.message.getMethod().equals(IcapMethod.OPTIONS)) {
			return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
		} else if(!encapsulated.containsEntry(IcapMessageElementEnum.REQHDR) & !encapsulated.containsEntry(IcapMessageElementEnum.RESHDR)) {
			return StateReturnValue.createRelevantResult(icapMessageDecoder.reset());
		}
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws DecodingException {
		IcapMessage message = icapMessageDecoder.message;
		if(message == null) {
			return null;
		}
		Encapsulated encapsulated = message.getEncapsulatedHeader();
		if(message.getMethod().equals(IcapMethod.OPTIONS)) {
			if(encapsulated.containsEntry(IcapMessageElementEnum.OPTBODY)) {
				return StateEnum.READ_CHUNK_SIZE_STATE;
			} else {
				return null;
			}
		} else {
			IcapMessageElementEnum entry = encapsulated.getNextEntry();
			if(entry != null) {
				if(entry.equals(IcapMessageElementEnum.REQHDR)) {
					return StateEnum.READ_HTTP_REQUEST_INITIAL_AND_HEADERS;
				} else if(entry.equals(IcapMessageElementEnum.RESHDR)) {
					return StateEnum.READ_HTTP_RESPONSE_INITIAL_AND_HEADERS;
				} else if(entry.equals(IcapMessageElementEnum.REQBODY)) {
					return StateEnum.READ_CHUNK_SIZE_STATE;
				} else if(entry.equals(IcapMessageElementEnum.RESBODY)) {
					return StateEnum.READ_CHUNK_SIZE_STATE;
				}
			}
		}
		return null;
	}
	
	private void validateMandatoryMessageHeaders(IcapMessage message, boolean isDecodingResponse) {
		if(!isDecodingResponse) {
			if(!message.containsHeader(IcapHeader.Names.HOST)) {
				throw new IcapDecodingError("Mandatory ICAP message header [Host] is missing");
			}
		}
		if(!message.containsHeader(IcapHeader.Names.ENCAPSULATED)) {
			throw new IcapDecodingError("Mandatory ICAP message header [Encapsulated] is missing");
		}
	}

}
