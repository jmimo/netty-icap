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


public abstract class State<T extends Object> {
	
	public State() {
	}
	
	/**
	 * Preparation method
	 */
	public abstract void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception;
	
	/**
	 * execution method
	 * @return @see {@link StateReturnValue} that contains, dependent on the relevance a return value.
	 */
	public abstract StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception;
	
	/**
	 * Flow decision method
	 * @return has to return a valid next state. Can be itself.
	 */
	public abstract StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, T decisionInformation) throws Exception;
	
	public boolean equals(Object object) {
		if(object != null && object instanceof State<?>) {
			return object.getClass().getName().equals(this.getClass().getName());
		}
		return false;
	}
}
