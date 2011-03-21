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

public enum StateEnum {
	SKIP_CONTROL_CHARS(new SkipControlCharsState()),
	READ_ICAP_INITIAL_STATE(new ReadIcapInitialState()),
	READ_ICAP_HEADER_STATE(new ReadIcapHeaderState()),
	READ_HTTP_REQUEST_INITIAL_AND_HEADERS(new ReadHttpRequestInitialAndHeadersState()),
	READ_HTTP_RESPONSE_INITIAL_AND_HEADERS(new ReadHttpResponseInitalAndHeadersState()),
//	PREVIEW_STATE(new PreviewState()),
	READ_CHUNK_SIZE_STATE(new ReadChunkSizeState()),
	READ_CHUNK_STATE(new ReadChunkState()),
	READ_CHUNKED_CONTENT_AS_CHUNKS_STATE(new ReadChunkedContentAsChunksState()),
	READ_CHUNK_DELIMITER_STATE(new ReadChunkDelimiterState()),
	READ_TRAILING_HEADERS_STATE(new ReadTrailingHeadersState());
	
	private State<? extends Object> state;
	
	StateEnum(State<? extends Object> state) {
		this.state = state;
	}
	
	public State<? extends Object> getState() {
		return state;
	}
}
