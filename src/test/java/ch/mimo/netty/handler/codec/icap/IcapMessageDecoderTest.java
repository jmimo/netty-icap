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

import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IcapMessageDecoderTest extends Assert {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapRequestDecoder());
	}
	
	@Test
	public void decodeOPTIONRequestTest() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSRequest());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateOPTIONSRequest(result);
	}
	
	@Test
	public void stripPrefixingWhitespacesFromMessage() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createWhiteSpacePrefixedOPTIONSRequest());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateWhiteSpacePrefixedOPTIONSRequest(result);
	}
	
	@Test
	public void decodeOPTIONSRequestWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSRequestWithBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertOPTIONSRequestWithBody(result);
		embedder.offer(DataMockery.createOPTIONSRequestWithBodyBodyChunk());
		IcapChunk dataChunk = (IcapChunk)embedder.poll();
		DataMockery.assertOPTIONSRequestWithBodyBodyChunk(dataChunk);
		embedder.offer(DataMockery.createOPTIONSRequestWithBodyLastChunk());
		IcapChunk lastChunk = (IcapChunk)embedder.poll();
		DataMockery.assertOPTIONSRequestWithBodyLastChunk(lastChunk);
	}
	
	@Test
	public void decodeREQMODRequestWithNullBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBodyAndReverseRequestAlignement() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyAndReverseRequestAlignement());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBodyAndReverseRequestAlignement(result);
	}
	
	@Test
	public void decodeREQMODRequestWithTwoChunkBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithTwoChunkBody(result);
		DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodyThirdChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeREQMODRequestWithPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithPreview());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithPreview(result);
		DataMockery.assertCreateREQMODWithPreviewChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithPreviewChunkLastChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeREQMODRequestWithEarlyTerminatedPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreview());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithEarlyTerminatedPreview((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithEarlyTerminatedPreviewLastChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeRESPMODWithGetRequestAndPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreview());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestAndPreview(result);
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewLastChunk((IcapChunk)embedder.poll());
	}
	
	// TODO options request with body
}

