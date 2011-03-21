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
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;

public class DefaultIcapChunk extends DefaultHttpChunk implements IcapChunk {

	private boolean preview;
	private boolean earlyTerminated;
	
	public DefaultIcapChunk(ChannelBuffer content) {
		super(content);
	}

	public DefaultIcapChunk(ChannelBuffer content, boolean isPreview, boolean isEarlyTerminated) {
		this(content);
		preview = isPreview;
		earlyTerminated = isEarlyTerminated;
	}
	
	@Override
	public boolean isPreviewChunk() {
		return preview;
	}

	@Override
	public boolean isEarlyTerminated() {
		return earlyTerminated;
	}
	
	public String toString() {
		return "DeafultIcapChunk: [isPreviewChunk=" + preview + "] [wasEarlyTerminated=" + earlyTerminated + "] [data=" + getContent().readableBytes() + "]";
	}
}