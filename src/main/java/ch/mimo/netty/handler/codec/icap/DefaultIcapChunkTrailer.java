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
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class DefaultIcapChunkTrailer implements IcapChunkTrailer {

	private boolean preview;
	private boolean earlyTerminated;
	
	public DefaultIcapChunkTrailer() {
		this.preview = false;
		this.earlyTerminated = false;
	}
	
	public DefaultIcapChunkTrailer(boolean isPreview, boolean isEarlyTerminated) {
		this();
		this.preview = isPreview;
		this.earlyTerminated = isEarlyTerminated;
	}
	
	@Override
	public void setIsPreviewChunk() {
		this.preview = true;
	}

	@Override
	public boolean isPreviewChunk() {
		return preview;
	}

	@Override
	public void setIsEarlyTerminated() {
		this.earlyTerminated = true;
	}

	@Override
	public boolean isEarlyTerminated() {
		return earlyTerminated;
	}

	@Override
	public ChannelBuffer getContent() {
		return ChannelBuffers.EMPTY_BUFFER;
	}

	@Override
	public void setContent(ChannelBuffer content) {
		// NOOP
	}

	@Override
	public boolean isLast() {
		return true;
	}

	@Override
	public String getHeader(String name) {
		return null;
	}

	@Override
	public List<String> getHeaders(String name) {
		return null;
	}

	@Override
	public List<Entry<String, String>> getHeaders() {
		return null;
	}

	@Override
	public boolean containsHeader(String name) {
		return false;
	}

	@Override
	public Set<String> getHeaderNames() {
		return null;
	}

	@Override
	public void addHeader(String name, Object value) {
	}

	@Override
	public void setHeader(String name, Object value) {	
	}

	@Override
	public void setHeader(String name, Iterable<?> values) {
	}

	@Override
	public void removeHeader(String name) {
	}

	@Override
	public void clearHeaders() {
	}
}
