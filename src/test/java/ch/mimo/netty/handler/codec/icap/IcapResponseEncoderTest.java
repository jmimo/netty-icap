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

public class IcapResponseEncoderTest extends AbstractEncoderTest {

	private EncoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() {
		embedder = new EncoderEmbedder<Object>(new IcapResponseEncoder());
	}
	
	@Test
	public void encode100ContinueResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.create100ContinueIcapResponse());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.create100ContinueResponse(),response);
	}
	
	@Test
	public void encodeOPTIONSResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSIcapResponse());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSResponse(),response);
	}
	
	@Test
	public void encodeOPTIONSResponseWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSIcapResponseWithBody());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSResponseWithBody(),response);
		embedder.offer(DataMockery.createOPTIONSIcapChunk());
		String chunk1 = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSChunk(),chunk1);
		embedder.offer(DataMockery.createOPTIONSLastIcapChunk());
		String lastChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createOPTIONSLastChunk(),lastChunk);
	}
	
	@Test
	public void encodeREQMODResponseWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyIcapResponse());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithGetRequestResponse(),response);
	}
	
	@Test
	public void encodeRESPMODResponseWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyIcapResponse());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createRESPMODWithGetRequestNoBodyResponse(),response);
	}
	
	@Test
	public void encodeREQMODResponseWithTwoChunkBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapResponse());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyResponse(),response);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		String chunk1 = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTowChunkBodyChunkOne(),chunk1);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
		String chunk2 = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyChunkTwo(),chunk2);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree());
		String chunk3 = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyChunkThree(),chunk3);
	}
	
	@Test
	public void encodeREQMODResponseWithHttpResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODResponseContainingHttpResponseIcapResponse());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODResponseContainingHttpResponse(),response);
	}
}
