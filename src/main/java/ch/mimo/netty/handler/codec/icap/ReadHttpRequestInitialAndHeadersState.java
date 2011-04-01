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
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class ReadHttpRequestInitialAndHeadersState extends State<Object> {

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
		HttpRequest message = new DefaultHttpRequest(HttpVersion.valueOf(initialLine[2]),HttpMethod.valueOf(initialLine[0]),initialLine[1]);
		icapMessageDecoder.message.setHttpRequest(message);
		List<String[]> headerList = IcapDecoderUtil.readHeaders(buffer,icapMessageDecoder.maxHttpHeaderSize);
		message.clearHeaders();
		for(String[] header : headerList) {
			message.addHeader(header[0],header[1]);
		}
		Encapsulated encapsulated = icapMessageDecoder.message.getEncapsulatedHeader();
		encapsulated.setProcessed(encapsulated.getNextEntry());
		if(encapsulated.getNextEntry() != null && encapsulated.getNextEntry().equals(EntryName.RESHDR)) {
			return StateReturnValue.createIrrelevantResult();
		}
		return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		Encapsulated encapsulated = icapMessageDecoder.message.getEncapsulatedHeader();
		EntryName entry = encapsulated.getNextEntry();
		if(entry != null) {
			if(entry.equals(EntryName.RESHDR)) {
				return StateEnum.READ_HTTP_RESPONSE_INITIAL_AND_HEADERS;
			}
			if(entry.equals(EntryName.REQBODY)) {
				return StateEnum.READ_CHUNK_SIZE_STATE;
			}
			if(entry.equals(EntryName.RESBODY)) {
				return StateEnum.READ_CHUNK_SIZE_STATE;
			}
		}
		return null;
	}

}
