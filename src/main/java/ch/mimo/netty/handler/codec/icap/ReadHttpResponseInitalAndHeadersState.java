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
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class ReadHttpResponseInitalAndHeadersState extends State<Object> {

	public ReadHttpResponseInitalAndHeadersState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
		if(icapMessageDecoder.message.getEncapsulatedHeader() == null) {
			throw new IllegalArgumentException("This state requires a valid Encapsulation header instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		String line = IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength);
		String[] initialLine = IcapDecoderUtil.splitInitialLine(line);
		HttpResponse message = new DefaultHttpResponse(HttpVersion.valueOf(initialLine[0]),HttpResponseStatus.valueOf(Integer.parseInt(initialLine[1])));
		icapMessageDecoder.message.setHttpResponse(message);
		List<String[]> headerList = IcapDecoderUtil.readHeaders(buffer,icapMessageDecoder.maxHttpHeaderSize);
		for(String[] header : headerList) {
			message.addHeader(header[0],header[1]);
		}
		Encapsulated encapsulated = icapMessageDecoder.message.getEncapsulatedHeader();
		encapsulated.setEntryAsProcessed(encapsulated.getNextEntry());
		if(encapsulated.getNextEntry() != null && encapsulated.getNextEntry().equals(IcapMessageElementEnum.REQHDR)) {
			return StateReturnValue.createIrrelevantResult();
		}
		return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		Encapsulated encapsulated = icapMessageDecoder.message.getEncapsulatedHeader();
		IcapMessageElementEnum entry = encapsulated.getNextEntry();
		if(entry != null) {
			if(entry.equals(IcapMessageElementEnum.REQHDR)) {
				return StateEnum.READ_HTTP_REQUEST_INITIAL_AND_HEADERS;
			}
			if(entry.equals(IcapMessageElementEnum.REQBODY)) {
				return StateEnum.READ_CHUNK_SIZE_STATE;
			}
			if(entry.equals(IcapMessageElementEnum.RESBODY)) {
				return StateEnum.READ_CHUNK_SIZE_STATE;
			}
		}
		return null;
	}

}
