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

import java.io.UnsupportedEncodingException;

import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapRequestEncoderTest extends AbstractEncoderTest {

	private EncoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() {
		embedder = new EncoderEmbedder<Object>(new IcapRequestEncoder());
	}
	
	@Test
	public void testEncoderWithUnknownObject() {
		embedder.offer(new String());
		assertNull("poll should return null",embedder.poll());
	}
	
	// TODO write test to force unsupported encoding exceptions while encoding
	
	@Test
	public void encodeOPTIONSRequest() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSIcapRequest());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSRequest(),request);
	}
	
	@Test
	public void encodeOPTIONSRequestWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSRequestWithBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSRequestWithBody(),request);
		embedder.offer(DataMockery.createOPTIONSRequestWithBodyBodyChunkIcapChunk());
		String dataChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSRequestWithBodyBodyChunk(),dataChunk);
		embedder.offer(DataMockery.createOPTIONSRequestWithBodyLastChunkIcapChunk());
		String lastChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSRequestWithBodyLastChunk(),lastChunk);
	}
	
	@Test
	public void encodeREQMODRequestWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		doOutput(request);
		assertResponse(DataMockery.createREQMODWithGetRequestNoBody(),request);
	}
	
	@Test
	public void encodeRESMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		doOutput(request);
		assertResponse(DataMockery.createRESPMODWithGetRequestNoBody(),request);
	}
	
	@Test
	public void encodeREQMODWithTwoChunkBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyAnnouncement(),request);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		String chunkOne = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTowChunkBodyChunkOne(),chunkOne);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
		String chunkTwo = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyChunkTwo(),chunkTwo);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree());
		String chunkThree = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyChunkThree(),chunkThree);
	}
	
	@Test
	public void encodeREQModWithTowChunkBodyAndTrailingHeader() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyAnnouncement(),request);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		String chunkOne = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTowChunkBodyChunkOne(),chunkOne);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
		String chunkTwo = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyChunkTwo(),chunkTwo);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyChunkThreeIcapChunkTrailer());
		String chunkThree = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyChunkThreeWithTrailer(),chunkThree);
	}
	
	@Test
	public void encodeREQMODWithPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithPreviewAnnouncementIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithPreviewAnnouncement(),request);
		embedder.offer(DataMockery.createREQMODWithPreviewIcapChunk());
		String previewChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithPreviewChunk(),previewChunk);
		embedder.offer(DataMockery.createREQMODWithPreviewLastIcapChunk());
		String lastChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithPreviewLastChunk(),lastChunk);
	}
	
	@Test
	public void encodeREQMODWithEarlyTerminatedPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreviewAnnouncementIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithEarlyTerminatedPreviewAnnouncement(),request);
		
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreviewIcapChunk());
		String previewChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithEarlyTerminatedPreviewChunk(),previewChunk);
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreviewLastIcapChunk());
		String lastChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithEarlyTerminatedPreviewLastChunk(),lastChunk);
	}
}
