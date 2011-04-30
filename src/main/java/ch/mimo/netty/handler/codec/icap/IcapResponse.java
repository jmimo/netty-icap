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

/**
 * ICAP response.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public interface IcapResponse extends IcapMessage {

	/**
	 * Sets the response status
	 * @param status @see {@link IcapResponseStatus} value like 200 OK.
	 */
	void setStatus(IcapResponseStatus status);
	
	/**
	 * Gets the response status for this message.
	 * 
	 * @return the response status as @see {@link IcapResponseStatus}
	 */
	IcapResponseStatus getStatus();
	
	/**
	 * Sets an OPTIONS body to this message.
	 * @param optionsContent @see {@link ChannelBuffer} containin the body.
	 */
	void setOptionsContent(ChannelBuffer optionsContent);

	/**
	 * Gets an OPTIONS body if present
	 * @return @see {@link ChannelBuffer} or null
	 */
	ChannelBuffer getOptionsContent();
}
