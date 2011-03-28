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
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class DefaultIcapRequest extends DefaultIcapMessage implements IcapRequest {
	
	private EntryName contentType;
	private ChannelBuffer content;
	
	public DefaultIcapRequest(HttpVersion icapVersion, HttpMethod method, String uri) {
		super(icapVersion,method,uri);
		content = ChannelBuffers.EMPTY_BUFFER;
		contentType = EntryName.NULLBODY;
	}

	@Override
	public void setContentType(EntryName type) {
		contentType = type;
	}

	@Override
	public EntryName getContentType() {
		return contentType;
	}
	
	@Override
	public void setContent(ChannelBuffer content) {
		this.content = content;
	}

	@Override
	public ChannelBuffer getContent() {
		return content;
	}
}