/*******************************************************************************
 * Copyright 2012 Michael Mimo Moratti
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
	
	private static final String SYNTHETIC_ENCAPSULATED_HEADER_VALUE = "null-body=0";
	
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
		boolean isRequest = icapMessageDecoder.message instanceof IcapRequest;
		boolean isOptionsRequest = isRequest && ((IcapRequest)icapMessageDecoder.message).getMethod().equals(IcapMethod.OPTIONS);
		
		handleEncapsulationHeaderVolatility(icapMessageDecoder.message);
		validateMandatoryMessageHeaders(icapMessageDecoder.message);
		
		Encapsulated encapsulated = null;
		String headerValue = icapMessageDecoder.message.getHeader(IcapHeaders.Names.ENCAPSULATED);
		if(headerValue != null) {
			encapsulated = new Encapsulated(icapMessageDecoder.message.getHeader(IcapHeaders.Names.ENCAPSULATED));
			icapMessageDecoder.message.setEncapsulatedHeader(encapsulated);
		}
		if(isOptionsRequest) {
			return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
		} else if(encapsulated != null && !encapsulated.containsEntry(IcapMessageElementEnum.REQHDR) && !encapsulated.containsEntry(IcapMessageElementEnum.RESHDR)) {
			return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
		}
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws DecodingException {
		IcapMessage message = icapMessageDecoder.message;
		Encapsulated encapsulated = message.getEncapsulatedHeader();
		if(message instanceof IcapRequest && ((IcapRequest)message).getMethod().equals(IcapMethod.OPTIONS)) {
			if(encapsulated != null && encapsulated.containsEntry(IcapMessageElementEnum.OPTBODY)) {
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
	
	private void validateMandatoryMessageHeaders(IcapMessage message) {
		if(!(message instanceof IcapResponse)) {
			if(!message.containsHeader(IcapHeaders.Names.HOST)) {
				throw new IcapDecodingError("Mandatory ICAP message header [Host] is missing");
			}
		}
		if(!message.containsHeader(IcapHeaders.Names.ENCAPSULATED)) {
			throw new IcapDecodingError("Mandatory ICAP message header [Encapsulated] is missing");
		}
	}

	/**
	 * This method handles the volatility problem of the Encapsulation header. In the RFC (3507) in section 4.4.1 is described 
	 * that the Encapsulated header MUST exist on all messages. This also contains OPTIONS request/response and 100,204 responses.
	 * 
	 * In the Errata section "When to send an Encapsulated Header" E1 is the MUST requirement weakened! It is now possible to 
	 * send OPTIONS requests and 100 Continue, 204 No Content responses without Encapsulated headers.
	 * 
	 * This method will create a synthetic Encapsulated header if necessary in order to simplify the downstream logic.
	 */
	private void handleEncapsulationHeaderVolatility(IcapMessage message) {
		// Pseudo code
		// IF Encapsulated header is missing
			// IF OPTIONS request OR 100 Continue response OR 204 No Content response OR is a server error
				// THEN inject synthetic null-body Encapsulated header.
		boolean requiresSynthecticEncapsulationHeader = false;
		if(!message.containsHeader(IcapHeaders.Names.ENCAPSULATED)) {
			if(message instanceof IcapRequest && ((IcapRequest)message).getMethod().equals(IcapMethod.OPTIONS)) {
				requiresSynthecticEncapsulationHeader = true;
			} else if(message instanceof IcapResponse) {
				IcapResponse response = (IcapResponse)message;
				IcapResponseStatus status = response.getStatus();
				if(status.equals(IcapResponseStatus.CONTINUE) ||
					status.equals(IcapResponseStatus.NO_CONTENT) ||
					(status.getCode() >= 500)) {
					requiresSynthecticEncapsulationHeader = true;
				}
			}
		}
		
		if(requiresSynthecticEncapsulationHeader) {
			message.addHeader(IcapHeaders.Names.ENCAPSULATED,SYNTHETIC_ENCAPSULATED_HEADER_VALUE);
		}
	}
}
