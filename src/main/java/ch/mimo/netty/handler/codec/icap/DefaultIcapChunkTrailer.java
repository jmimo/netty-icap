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

import org.jboss.netty.handler.codec.http.DefaultHttpChunkTrailer;

/**
 * This class is used to indicate the end of a chunked data stream and to hold 
 * trailing headers.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapChunkTrailer implementation.
 *
 */
public class DefaultIcapChunkTrailer extends DefaultHttpChunkTrailer implements IcapChunkTrailer {
	
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
	public void setPreviewChunk(boolean preview) {
		this.preview = preview;
	}

	@Override
	public boolean isPreviewChunk() {
		return preview;
	}

	@Override
	public void setEarlyTermination(boolean earlyTermination) {
		this.earlyTerminated = earlyTermination;
	}

	@Override
	public boolean isEarlyTerminated() {
		return earlyTerminated;
	}
	
	public String toString() {
		return "DeafultIcapChunkTrailer: [isPreviewChunk=" + preview + "] [wasEarlyTerminated=" + earlyTerminated + "]";
	}
}
