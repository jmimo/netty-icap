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

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Main Icap Response implementation. This is the starting point to create any Icap response.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class DefaultIcapResponse extends AbstractIcapMessage implements IcapResponse {

	private IcapResponseStatus status;
	private ChannelBuffer optionsContent;
	
	/**
	 * Will create an instance of IcapResponse.
	 * 
	 * @param version the version of the response.
	 * @param status the Status code that has to be reported back. (200 OK...)
	 */
	public DefaultIcapResponse(IcapVersion version, IcapResponseStatus status) {
		super(version);
		this.status = status;
	}

	@Override
	public void setStatus(IcapResponseStatus status) {
		this.status = status;
	}

	@Override
	public IcapResponseStatus getStatus() {
		return status;
	}

	public void setContent(ChannelBuffer optionsContent) {
		this.optionsContent = optionsContent;
	}

	public ChannelBuffer getContent() {
		return optionsContent;
	}

	@Override
	public String toString() {
		return super.toString() + StringUtil.NEWLINE + "Response Status: " + status.name();
	}
}
