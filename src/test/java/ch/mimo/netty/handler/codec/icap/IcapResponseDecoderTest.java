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

import org.jboss.netty.handler.codec.embedder.CodecEmbedderException;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapResponseDecoderTest extends AbstractIcapTest {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapResponseDecoder());
	}
	
	@Test
	public void decodeOPTIONSResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertOPTIONSResponse(response);
	}
	
	@Test
	public void decodeREQMODResponseWithGetRequestNoBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertREQMODWithGetRequestResponse(response);
	}
	
	@Test
	public void decodeRESPMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertRESPMODWithGetRequestNoBodyResponse(response);
	}

	@Test
	public void decodeREQMODResponseWithGetRequestAndBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithImplicitTwoChunkBodyResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertCreateREQMODWithImplicitTwoChunkBodyResponse(response);
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk(chunk1);
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk(chunk2);
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBodyThirdChunk(chunk3);
	}
	
	@Test 
	public void decodeRESPMODWithGetRequestAndPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreviewResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewResponse(response);
		IcapChunk previewChunk = (IcapChunk)embedder.poll();
		doOutput(previewChunk.toString());
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewChunk(previewChunk);
		IcapChunk lastChunk = (IcapChunk)embedder.poll();
		doOutput(lastChunk.toString());
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewLastChunk(lastChunk);
	}
	
	@Test
	public void decode100Continue() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.create100ContinueResponse());
		IcapResponse result = (IcapResponse)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		assertEquals("wrong response status code",IcapResponseStatus.CONTINUE,result.getStatus());
	}
	
	@Test
	public void decode100ContineFollowedBy204NoContent() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.create100ContinueResponse());
		IcapResponse result = (IcapResponse)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		assertEquals("wrong response status code",IcapResponseStatus.CONTINUE,result.getStatus());
		embedder.offer(DataMockery.create204NoContentResponse());
		result = (IcapResponse)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		assertEquals("wrong response status code",IcapResponseStatus.NO_CONTENT,result.getStatus());
	}
	
	@Test
	public void decodeRESPMODWithPreviewFollowedByREQPMODFollwedBy100Continue() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreviewResponse());
		Object object = embedder.poll();
		assertNotNull("RESPMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapResponse);
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
		embedder.offer(DataMockery.createREQMODWithGetRequestResponse());
		object = embedder.poll();
		assertNotNull("REQMOD request was null",object);
		assertTrue("wrong object type",object instanceof IcapResponse);
		embedder.offer(DataMockery.create100ContinueResponse());
		IcapResponse result = (IcapResponse)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		assertEquals("wrong response status code",IcapResponseStatus.CONTINUE,result.getStatus());
	}
	
	// TODO add missing Encapsulation header functionality.
	@Test(expected=CodecEmbedderException.class)
	public void decodeREQMODResponseWithHttpResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.create204ResponseWithoutEncapsulatedHeader());
		embedder.poll();
	}
	
	@Test
	public void decode204ResponseWithoutEncapsulatedHeader() throws UnsupportedEncodingException {
		
	}
}
