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

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class ReadIcapHeaderState extends State<Object> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		List<String[]> headerList = IcapDecoderUtil.readHeaders(buffer,icapMessageDecoder.maxIcapHeaderSize);
		icapMessageDecoder.message.clearHeaders();
		for(String[] header : headerList) {
			icapMessageDecoder.message.addHeader(header[0],header[1]);
		}
		// validate mandatory icap headers
		if(!icapMessageDecoder.message.containsHeader(IcapHeaders.Names.HOST)) {
			throw new Error("Mandatory ICAP message header [Host] is missing");
		}
		if(!icapMessageDecoder.message.containsHeader(IcapHeaders.Names.ENCAPSULATED)) {
			throw new Error("Mandatory ICAP message header [Encapsulated] is missing");
		}
		Encapsulated encapsulated = Encapsulated.parseHeader(icapMessageDecoder.message.getHeader(IcapHeaders.Names.ENCAPSULATED));
		icapMessageDecoder.message.setEncapsulatedHeader(encapsulated);
		
		if((!encapsulated.containsEntry(EntryName.REQHDR) & !encapsulated.containsEntry(EntryName.RESHDR)) | 
				icapMessageDecoder.message.getMethod().equals(IcapMethod.OPTIONS)) {
			return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
		}
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		IcapMessage message = icapMessageDecoder.message;
		Encapsulated encapsulated = message.getEncapsulatedHeader();
		if(message.getMethod().equals(IcapMethod.OPTIONS) & encapsulated.containsEntry(EntryName.OPTBODY)) {
			// TODO validate options request with body
//			if(icapMessageDecoder.message.isPreview() & Integer.parseInt(icapMessageDecoder.message.getHeader("Preview")) > 0) {
//				return StateEnum.PREVIEW_STATE;
//			} else {
//				return StateEnum.READ_CHUNK_SIZE_STATE;
//			}
		} else {
			if(message.getMethod().equals(IcapMethod.OPTIONS)) {
				return null;
			}
			EntryName entry = encapsulated.getNextEntry();
			if(entry != null) {
				if(entry.equals(EntryName.REQHDR)) {
					return StateEnum.READ_HTTP_REQUEST_INITIAL_AND_HEADERS;
				}
				if(entry.equals(EntryName.RESHDR)) {
					return StateEnum.READ_HTTP_RESPONSE_INITIAL_AND_HEADERS;
				}
				if(entry.equals(EntryName.REQBODY) | entry.equals(EntryName.RESBODY)) {
					return StateEnum.READ_CHUNK_SIZE_STATE;
				}
			}
		}
		return null;
	}

}
