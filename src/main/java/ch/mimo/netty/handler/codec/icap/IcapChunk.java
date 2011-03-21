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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpChunk;

public interface IcapChunk extends HttpChunk {

	static IcapChunkTrailer LAST_ICAP_CHUNK = new IcapChunkTrailer() {
		
		private boolean preview;
		private boolean earlyTerminated;
		
        public ChannelBuffer getContent() {
            return ChannelBuffers.EMPTY_BUFFER;
        }

        public void setContent(ChannelBuffer content) {
            throw new IllegalStateException("read-only");
        }

        public boolean isLast() {
            return true;
        }

        public void addHeader(String name, Object value) {
            throw new IllegalStateException("read-only");
        }

        public void clearHeaders() {
            // NOOP
        }

        public boolean containsHeader(String name) {
            return false;
        }

        public String getHeader(String name) {
            return null;
        }

        public Set<String> getHeaderNames() {
            return Collections.emptySet();
        }

        public List<String> getHeaders(String name) {
            return Collections.emptyList();
        }

        public List<Map.Entry<String, String>> getHeaders() {
            return Collections.emptyList();
        }

        public void removeHeader(String name) {
            // NOOP
        }

        public void setHeader(String name, Object value) {
            throw new IllegalStateException("read-only");
        }

        public void setHeader(String name, Iterable<?> values) {
            throw new IllegalStateException("read-only");
        }

        public void setIsPreviewChunk() {
        	preview = true;
        }
        
		public boolean isPreviewChunk() {
			return preview;
		}
		
		public void setIsEarlyTerminated() {
			earlyTerminated = true;
		}
		
		public boolean isEarlyTerminated() {
			return earlyTerminated;
		}
	};
	
	void setIsPreviewChunk();
	
	boolean isPreviewChunk();
	
	void setIsEarlyTerminated();
	
	boolean isEarlyTerminated();
}
