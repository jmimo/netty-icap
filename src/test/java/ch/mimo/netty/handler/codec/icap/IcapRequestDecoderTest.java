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
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateOPTIONSRequest(result);
	}
	
	@Test
	public void stripPrefixingWhitespacesFromMessage() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createWhiteSpacePrefixedOPTIONSRequest());
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateWhiteSpacePrefixedOPTIONSRequest(result);
	}
	
	@Test
	public void decodeOPTIONSRequestWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSRequestWithBody());
		IcapRequest result = (IcapRequest)embedder.poll();
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
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBody());
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBodyAndReverseRequestAlignement() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyAndReverseRequestAlignement());
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBodyAndReverseRequestAlignement(result);
	}
	
	@Test
	public void decodeREQMODRequestWithTwoChunkBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithTwoChunkBody(result);
		DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodyThirdChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeREQMODRequestWithTwoChunkBodyAndTrailingHeaders() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyAndTrailingHeaders());
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithTwoChunkBody(result);
		DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodyTrailingHeaderChunk((IcapChunkTrailer)embedder.poll());
	}
	
	@Test
	public void decodeREQMODRequestWithPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithPreview());
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithPreview(result);
		DataMockery.assertCreateREQMODWithPreviewChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithPreviewChunkLastChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeREQMODRequestWithPreviewExpectingChunkTrailer() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithPreview());
		IcapRequest result = (IcapRequest)embedder.poll();
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
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestAndPreview(result);
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewLastChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeREQMODWithGetRequestAndHugeChunk() throws UnsupportedEncodingException {
		DecoderEmbedder<Object> embedder = new DecoderEmbedder<Object>(new IcapRequestDecoder(4000,4000,4000,10));
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		IcapRequest result = (IcapRequest)embedder.poll();
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
	
	@Test
	public void decodeRESPMODWithGetRequestAndPreviewAndHugeChunk() throws UnsupportedEncodingException {
		DecoderEmbedder<Object> embedder = new DecoderEmbedder<Object>(new IcapRequestDecoder(4000,4000,4000,10));
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreview());
		IcapRequest result = (IcapRequest)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestAndPreview(result);
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertEquals("chunk 1 has wrong contents","This is da",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		assertTrue("chunk 1 is not marked as preview chunk",chunk1.isPreviewChunk());
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertEquals("chunk 2 has wrong contents","ta that wa",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		assertTrue("chunk 2 is not marked as preview chunk",chunk2.isPreviewChunk());
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertEquals("chunk 3 has wrong contents","s returned",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		assertTrue("chunk 3 is not marked as preview chunk",chunk3.isPreviewChunk());
		IcapChunk chunk5 = (IcapChunk)embedder.poll();
		assertEquals("chunk 5 has wrong contents"," by an ori",chunk5.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		assertTrue("chunk 5 is not marked as preview chunk",chunk5.isPreviewChunk());
		IcapChunk chunk6 = (IcapChunk)embedder.poll();
		assertEquals("chunk 6 has wrong contents","gin server",chunk6.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		assertTrue("chunk 6 is not marked as preview chunk",chunk6.isPreviewChunk());
		IcapChunk chunk7 = (IcapChunk)embedder.poll();
		assertEquals("chunk 7 has wrong contents",".",chunk7.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		assertTrue("chunk 7 is not marked as preview chunk",chunk7.isPreviewChunk());
		IcapChunk chunk8 = (IcapChunk)embedder.poll();
		assertTrue("last chunk is of wrong type",chunk8 instanceof IcapChunkTrailer);
		assertTrue("last chunk is not marked as such",chunk8.isLast());
		assertTrue("last chunk is not marked as preview chunk",chunk8.isPreviewChunk());
	}
	
	@Test
	public void decodeREQMODfollowedByRESPMODbothWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBody());
		Object object = embedder.poll();
		assertNotNull("REQMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest reqmodRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.REQMOD,reqmodRequest.getMethod());
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBody());
		object = embedder.poll();
		assertNotNull("RESPMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest respmodRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.RESPMOD,respmodRequest.getMethod());
	}
	
	@Test
	public void decodeREQMODFollowedByRESPMODWithPreviewFollowedByRESPMODFollowedByOPTIONS() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBody());
		Object object = embedder.poll();
		assertNotNull("REQMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest reqmodRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.REQMOD,reqmodRequest.getMethod());
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreview());
		object = embedder.poll();
		assertNotNull("RESPMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest respmodRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.RESPMOD,respmodRequest.getMethod());
		object = embedder.poll();
		assertNotNull("RESPMOD preview chunk was null",object);
		assertTrue("wrong object type",object instanceof IcapChunk);
		IcapChunk chunk = (IcapChunk)object;
		assertTrue("chunk is not preview",chunk.isPreviewChunk());
		object = embedder.poll();
		assertNotNull("preview chunk trailer is null",object);
		assertTrue("wrong object type",object instanceof IcapChunkTrailer);
		IcapChunkTrailer trailer = (IcapChunkTrailer)object;
		assertTrue("chunk trailer is not marked as preview",trailer.isPreviewChunk());
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBody());
		object = embedder.poll();
		assertNotNull("RESPMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest respmodRequest1 = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.RESPMOD,respmodRequest1.getMethod());
		embedder.offer(DataMockery.createOPTIONSRequest());
		object = embedder.poll();
		assertNotNull("options request is null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest optionsRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.OPTIONS,optionsRequest.getMethod());
	}
	
	@Test
	public void decodeREQMODWithTwoChunkBodyFollowedByRESPMODWithPreviewFollowedByRESMODNoBodyFollowedByOPTIONSRequest() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		Object object = embedder.poll();
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest respmodRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.REQMOD,respmodRequest.getMethod());
		object = embedder.poll();
		assertNotNull("REQMOD preview chunk was null",object);
		assertTrue("wrong object type",object instanceof IcapChunk);
		object = embedder.poll();
		assertNotNull("REQMOD preview chunk was null",object);
		assertTrue("wrong object type",object instanceof IcapChunk);
		object = embedder.poll();
		assertNotNull("preview chunk trailer is null",object);
		assertTrue("wrong object type",object instanceof IcapChunkTrailer);
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreview());
		object = embedder.poll();
		assertNotNull("RESPMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		respmodRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.RESPMOD,respmodRequest.getMethod());
		object = embedder.poll();
		assertNotNull("RESPMOD preview chunk was null",object);
		assertTrue("wrong object type",object instanceof IcapChunk);
		IcapChunk chunk = (IcapChunk)object;
		assertTrue("chunk is not preview",chunk.isPreviewChunk());
		object = embedder.poll();
		assertNotNull("preview chunk trailer is null",object);
		assertTrue("wrong object type",object instanceof IcapChunkTrailer);
		IcapChunkTrailer trailer = (IcapChunkTrailer)object;
		assertTrue("chunk trailer is not marked as preview",trailer.isPreviewChunk());
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBody());
		object = embedder.poll();
		assertNotNull("RESPMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest respmodRequest1 = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.RESPMOD,respmodRequest1.getMethod());
		embedder.offer(DataMockery.createOPTIONSRequest());
		object = embedder.poll();
		assertNotNull("options request is null",object);
		assertTrue("wrong object type",object instanceof IcapRequest);
		IcapRequest optionsRequest = (IcapRequest)object;
		assertEquals("wrong request method",IcapMethod.OPTIONS,optionsRequest.getMethod());
	}
}

