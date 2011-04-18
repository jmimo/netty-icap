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
import org.junit.Before;
import org.junit.Test;

public class IcapRequestDecoderTest extends AbstractIcapTest {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapRequestDecoder());
	}
	
	@Test 
	public void testConstructorValueValidation() {
		boolean error = false;
		try {
			new IcapRequestDecoder(0,1,1,1);
		} catch(IllegalArgumentException iage) {
			error = true;
		}
		assertTrue("No exception was thrown for the maxInitialLength validation",error);
		error = false;
		try {
			new IcapRequestDecoder(1,0,1,1);
		} catch(IllegalArgumentException iage) {
			error = true;
		}
		assertTrue("No exception was thrown for the maxIcapHeaderSize validation",error);
		error = false;
		error = false;
		try {
			new IcapRequestDecoder(1,1,0,1);
		} catch(IllegalArgumentException iage) {
			error = true;
		}
		assertTrue("No exception was thrown for the maxHttpHeaderSize validation",error);
		error = false;
		error = false;
		try {
			new IcapRequestDecoder(1,1,1,0);
		} catch(IllegalArgumentException iage) {
			error = true;
		}
		assertTrue("No exception was thrown for the maxChunkSize validation",error);
		error = false;
		try {
			new IcapRequestDecoder(1,1,1,1);
		} catch(IllegalArgumentException iage) {
			error = true;
		}
		assertFalse("All input values are greater null but exception occured",error);
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
	public void decodeREQMODRequestWithTwoChunkBodyAndTrailingHeaders() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyAndTrailingHeaders());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithTwoChunkBody(result);
		DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodyTrailingHeaderChunk((IcapChunkTrailer)embedder.poll());
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
	public void decodeREQMODRequestWithPreviewExpectingChunkTrailer() throws UnsupportedEncodingException {
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
	
	@Test
	public void decodeREQMODWithGetRequestAndHugeChunk() throws UnsupportedEncodingException {
		DecoderEmbedder<Object> embedder = new DecoderEmbedder<Object>(new IcapRequestDecoder(4000,4000,4000,10));
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithTwoChunkBody(result);
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertEquals("chunk 1 has wrong contents","This is da",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertEquals("chunk 2 has wrong contents","ta that wa",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertEquals("chunk 3 has wrong contents","s returned",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk4 = (IcapChunk)embedder.poll();
		assertEquals("chunk 4 has wrong contents"," by an ori",chunk4.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk5 = (IcapChunk)embedder.poll();
		assertEquals("chunk 5 has wrong contents","gin server",chunk5.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk6 = (IcapChunk)embedder.poll();
		assertEquals("chunk 6 has wrong contents",".",chunk6.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk7 = (IcapChunk)embedder.poll();
		assertEquals("chunk 7 has wrong contents","And this t",chunk7.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk8 = (IcapChunk)embedder.poll();
		assertEquals("chunk 8 has wrong contents","he second ",chunk8.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk9 = (IcapChunk)embedder.poll();
		assertEquals("chunk 9 has wrong contents","chunk whic",chunk9.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk10 = (IcapChunk)embedder.poll();
		assertEquals("chunk 10 has wrong contents","h contains",chunk10.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk11 = (IcapChunk)embedder.poll();
		assertEquals("chunk 11 has wrong contents"," more info",chunk11.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk12 = (IcapChunk)embedder.poll();
		assertEquals("chunk 12 has wrong contents","rmation.",chunk12.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk13 = (IcapChunk)embedder.poll();
		assertTrue("last chunk is of wrong type",chunk13 instanceof IcapChunkTrailer);
		assertTrue("last chunk is not marked as such",chunk13.isLast());
	}
}

