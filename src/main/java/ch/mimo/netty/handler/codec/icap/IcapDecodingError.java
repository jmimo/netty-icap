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

public class IcapDecodingError extends Error {

	private static final long serialVersionUID = 485693202925398675L;
	
	public IcapDecodingError() {
		super();
	}

	public IcapDecodingError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IcapDecodingError(String arg0) {
		super(arg0);
	}

	public IcapDecodingError(Throwable arg0) {
		super(arg0);
	}
}
