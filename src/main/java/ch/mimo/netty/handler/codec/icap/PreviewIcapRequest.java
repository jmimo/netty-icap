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

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class PreviewIcapRequest extends DefaultIcapRequest implements IcapRequestWithPreview {
	
	private List<IcapChunk> previewChunks;
	
	public PreviewIcapRequest(HttpVersion icapVersion, HttpMethod method, String uri) {
		super(icapVersion,method,uri);
		super.addHeader(IcapHeaders.Names.PREVIEW,"0");
		previewChunks = new ArrayList<IcapChunk>();
	}

	@Override
	public void addPreviewChunk(IcapChunk chunk) {
		int preview = Integer.parseInt(super.getHeader(IcapHeaders.Names.PREVIEW));
		preview += chunk.getContent().readableBytes();
		super.removeHeader(IcapHeaders.Names.PREVIEW);
		super.addHeader(IcapHeaders.Names.PREVIEW,Integer.toString(preview));
		previewChunks.add(chunk);
	}

	@Override
	public IcapChunk[] getPreviewChunks() {
		return previewChunks.toArray(new IcapChunk[]{});
	}
}
