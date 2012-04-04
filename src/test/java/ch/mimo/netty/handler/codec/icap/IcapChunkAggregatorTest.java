/*******************************************************************************
 * Copyright 2011 - 2012 Michael Mimo Moratti
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapChunkAggregatorTest extends AbstractIcapTest {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapChunkAggregator(4012));
	}
	
	@Test
	public void offerUnknownObject() {
		embedder.offer("The ultimate answer is 42");
	}
	
	@Test
	public void retrieveOptionsBody() {
		ChannelBuffer buffer = IcapChunkAggregator.extractHttpBodyContentFromIcapMessage(DataMockery.createOPTIONSResponseWithBodyAndContentIcapResponse());
		assertNotNull("buffer was null",buffer);
	}
	
	@Test
	public void retrieveHttpRequestBody() {
		ChannelBuffer buffer = IcapChunkAggregator.extractHttpBodyContentFromIcapMessage(DataMockery.createREQMODWithBodyContentIcapMessage());
		assertNotNull("buffer was null",buffer);
	}
	
	@Test
	public void retrieveHttpResponseBody() {
		ChannelBuffer buffer = IcapChunkAggregator.extractHttpBodyContentFromIcapMessage(DataMockery.createRESPMODWithPreviewDataIcapRequest());
		assertNotNull("buffer was null",buffer);
	}
	
	
	@Test
	public void aggregatorOPTIONSResponseWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSResponseWithBodyIcapResponse());
		embedder.offer(DataMockery.createOPTIONSRequestWithBodyBodyChunkIcapChunk());
		embedder.offer(DataMockery.createOPTIONSRequestWithBodyLastChunkIcapChunk());
		IcapResponse response = (IcapResponse)embedder.poll();
		assertNotNull("response was null",response);
		assertEquals("wrong body value in response",IcapMessageElementEnum.OPTBODY,response.getBodyType());
		assertNotNull("no body in options response",response.getContent());
		ChannelBuffer buffer = response.getContent();
		assertEquals("body was wrong","This is a options body chunk.",buffer.toString(Charset.defaultCharset()));
	}
	
	@Test
	public void aggregatorREQMODWithGetRequestWithoutChunks() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyAndEncapsulationHeaderIcapMessage());
		IcapRequest request = (IcapRequest)embedder.poll();
		DataMockery.assertCreateREQMODWithGetRequestNoBody(request);
	}
	
	@Test
	public void aggregatorREQMODWithGetRequestWithoutChunksAndNullBodySet() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyAndEncapsulationHeaderAndNullBodySetIcapMessage());
		IcapRequest request = (IcapRequest)embedder.poll();
		DataMockery.assertCreateREQMODWithGetRequestNoBody(request);
	}
	
	@Test
	public void aggregatorChunkOnlyTest() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreviewIcapChunk());
		IcapChunk chunk = (IcapChunk)embedder.poll();
		assertNotNull("no chunk received from pipeline",chunk);
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewChunk(chunk);
	}
	
	@Test
	public void aggregatorMessageWithoutBodyFollowedByBodyChunk() {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyAndEncapsulationHeaderIcapMessage());
		IcapRequest request = (IcapRequest)embedder.poll();
		DataMockery.assertCreateREQMODWithGetRequestNoBody(request);
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreviewIcapChunk());
		IcapChunk chunk = (IcapChunk)embedder.poll();
		assertNotNull("no chunk received from pipeline",chunk);
		DataMockery.assertCreateRESPMODWithGetRequestAndPreviewChunk(chunk);
	}
	
	@Test
	public void aggregateREQMODRequestWithChunks() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyAndEncapsulationHeaderIcapMessage());
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree());
		IcapRequest request = (IcapRequest)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBody(request);
		String body = request.getHttpRequest().getContent().toString(IcapCodecUtil.ASCII_CHARSET);
		StringBuilder builder = new StringBuilder();
		builder.append("This is data that was returned by an origin server.");
		builder.append("And this the second chunk which contains more information.");
		assertEquals("The body content was wrong",builder.toString(),body);
		Object object = embedder.peek();
		assertNull("still something there",object);
	}
	
	@Test
	public void aggregateRESPMODRequestWithPreviewChunks() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreviewIncludingEncapsulationHeaderIcapRequest());
		embedder.offer(DataMockery.createRESPMODWithGetRequestAndPreviewIcapChunk());
		embedder.offer(DataMockery.crateRESPMODWithGetRequestAndPreviewLastIcapChunk());
		IcapRequest request = (IcapRequest)embedder.poll();
		DataMockery.assertCreateRESPMODWithGetRequestAndPreview(request);
		String body = request.getHttpResponse().getContent().toString(IcapCodecUtil.ASCII_CHARSET);
		StringBuilder builder = new StringBuilder();
		builder.append("This is data that was returned by an origin server.");
		assertEquals("The body content was wrong",builder.toString(),body);
		Object object = embedder.peek();
		assertNull("still something there",object);
	}
	
	@Test
	public void aggregateREQModRequestWithCunksAndTrailingHeaders() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyAndEncapsulationHeaderIcapMessage());
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyChunkThreeIcapChunkTrailer());
		IcapRequest request = (IcapRequest)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBody(request);
		assertTrue("Key does not exist [TrailingHeaderKey1]",request.getHttpRequest().containsHeader("TrailingHeaderKey1"));
		assertEquals("The header: TrailingHeaderKey1 is invalid","TrailingHeaderValue1",request.getHttpRequest().getHeader("TrailingHeaderKey1"));
		assertTrue("Key does not exist [TrailingHeaderKey2]",request.getHttpRequest().containsHeader("TrailingHeaderKey2"));
		assertEquals("The header: TrailingHeaderKey2 is invalid","TrailingHeaderValue2",request.getHttpRequest().getHeader("TrailingHeaderKey2"));
		assertTrue("Key does not exist [TrailingHeaderKey3]",request.getHttpRequest().containsHeader("TrailingHeaderKey3"));
		assertEquals("The header: TrailingHeaderKey3 is invalid","TrailingHeaderValue3",request.getHttpRequest().getHeader("TrailingHeaderKey3"));
		assertTrue("Key does not exist [TrailingHeaderKey4]",request.getHttpRequest().containsHeader("TrailingHeaderKey1"));
		assertEquals("The header: TrailingHeaderKey4 is invalid","TrailingHeaderValue4",request.getHttpRequest().getHeader("TrailingHeaderKey4"));
		String body = request.getHttpRequest().getContent().toString(IcapCodecUtil.ASCII_CHARSET);
		StringBuilder builder = new StringBuilder();
		builder.append("This is data that was returned by an origin server.");
		builder.append("And this the second chunk which contains more information.");
		assertEquals("The body content was wrong",builder.toString(),body);
		Object object = embedder.peek();
		assertNull("still something there",object);
	}
	
	@Test
	public void exceedMaximumBodySize() throws UnsupportedEncodingException {
		DecoderEmbedder<Object> embedder = new DecoderEmbedder<Object>(new IcapChunkAggregator(20));
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyAndEncapsulationHeaderIcapMessage());
		boolean exception = false;
		try {
			embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		} catch(RuntimeException rte) {
			exception = true;
		}
		assertTrue("No Exception was thrown",exception);
	}
}

