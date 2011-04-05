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

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class DefaultIcapRequest extends DefaultIcapMessage implements IcapRequest {
	
	public DefaultIcapRequest(HttpVersion icapVersion, HttpMethod method, String uri, String host) {
		super(icapVersion,method,uri);
		addHeader(IcapHeaders.Names.HOST,host);
	}

	@Override
	protected void validateHeader(String name) throws IllegalArgumentException {
		if(name.equalsIgnoreCase(HttpHeaders.Names.TRANSFER_ENCODING)) {
			throw new IllegalArgumentException("The header [" + name + "] is not allowed in a ICAP request");
		}
	}
}