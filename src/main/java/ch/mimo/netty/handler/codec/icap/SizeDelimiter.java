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

import org.jboss.netty.handler.codec.frame.TooLongFrameException;

/**
 * This class is used to track the size in bytes of headers.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapDecoderUtil
 * @see ReadTrailingHeadersState
 */
public class SizeDelimiter {

	private int counter = 0;
	private int limit;
	private String errorMessage;
	
	public SizeDelimiter(int limit) {
		this.limit = limit;
		this.errorMessage = "limit exeeded by: ";
	}
	
	public synchronized void increment(int count) throws TooLongFrameException {
		counter += count;
		checkLimit();
	}
	
	public void increment() throws TooLongFrameException {
		this.increment(1);
	}
	
	public int getSize() {
		return counter;
	}
	
	private void checkLimit() throws TooLongFrameException {
		if(counter >= limit) {
			throw new TooLongFrameException(errorMessage + "[" + (counter - limit) + "] counts");
		}
	}
}
