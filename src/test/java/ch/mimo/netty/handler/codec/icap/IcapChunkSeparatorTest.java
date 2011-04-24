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

public class IcapChunkSeparatorTest extends AbstractIcapTest {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapChunkSeparator(20));
	}
	
	@Test
	public void sendNonIcapMessage() {
		embedder.offer("This is a simple string");
		String message = (String)embedder.poll();
		assertNotNull("input response was not received",message);
		assertEquals("input message is not equals output message","This is a simple string",message);
	}
	
	@Test
	public void separateREQMODWithGetRequestNoBodyIcapRequest() {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyIcapMessage());
		IcapMessage message = (IcapMessage)embedder.poll();
		assertNotNull("message was null",message);
		assertNull("still some elements in the pipeline",embedder.poll());
	}
	
	@Test
	public void separateREQMODWithGetRequestAndData() {
		embedder.offer(DataMockery.createREQMODWithGetRequestAndDataIcapMessage());
		IcapMessage message = (IcapMessage)embedder.poll();
		assertNotNull("message was null",message);
		assertEquals("message body indicator is wrong",IcapMessageElementEnum.REQBODY,message.getBody());
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 1 was null",chunk1);
		assertEquals("chunk 1 content is wrong","This is data that wa",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 2 was null",chunk2);
		assertEquals("chunk 2 content is wrong","s returned by an ori",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 3 was null",chunk3);
		assertEquals("chunk 3 content is wrong","gin server.",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunkTrailer trailer = (IcapChunkTrailer)embedder.poll();
		assertNotNull("chunk trailer was null",trailer);
	}
	
	@Test
	public void separateREQMODWithGetRequestAndDataResponse() {
		embedder.offer(DataMockery.createREQMODWithDataIcapResponse());
		IcapMessage message = (IcapMessage)embedder.poll();
		assertNotNull("message was null",message);
		assertEquals("message body indicator is wrong",IcapMessageElementEnum.REQBODY,message.getBody());
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 1 was null",chunk1);
		assertEquals("chunk 1 content is wrong","This is data that wa",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 2 was null",chunk2);
		assertEquals("chunk 2 content is wrong","s returned by an ori",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 3 was null",chunk3);
		assertEquals("chunk 3 content is wrong","gin server.",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunkTrailer trailer = (IcapChunkTrailer)embedder.poll();
		assertNotNull("chunk trailer was null",trailer);
	}
	
	@Test
	public void separateREQMODWithPreviewData() {
		embedder.offer(DataMockery.createRESPMODWithPreviewDataIcapRequest());
		IcapMessage message = (IcapMessage)embedder.poll();
		assertNotNull("message was null",message);
		assertEquals("message body indicator is wrong",IcapMessageElementEnum.RESBODY,message.getBody());
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 1 was null",chunk1);
		assertTrue("chunk 1 is not marked as preview",chunk1.isPreviewChunk());
		assertEquals("chunk 1 content is wrong","This is data that wa",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 2 was null",chunk2);
		assertTrue("chunk 2 is not marked as preview",chunk2.isPreviewChunk());
		assertEquals("chunk 2 content is wrong","s returned by an ori",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 3 was null",chunk3);
		assertTrue("chunk 3 is not marked as preview",chunk3.isPreviewChunk());
		assertEquals("chunk 3 content is wrong","gin server.",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunkTrailer trailer = (IcapChunkTrailer)embedder.poll();
		assertNotNull("chunk trailer was null",trailer);
		assertTrue("trailer is not marked as preview",trailer.isPreviewChunk());
	}
	
	@Test
	public void separateREQMODWithPreviewDataAndEarlyTermination() {
		embedder.offer(DataMockery.createRESPMODWithPreviewDataAndEarlyTerminationIcapRequest());
		IcapMessage message = (IcapMessage)embedder.poll();
		assertNotNull("message was null",message);
		assertEquals("message body indicator is wrong",IcapMessageElementEnum.RESBODY,message.getBody());
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 1 was null",chunk1);
		assertTrue("chunk 1 is not marked as preview",chunk1.isPreviewChunk());
		assertEquals("chunk 1 content is wrong","This is data that wa",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 2 was null",chunk2);
		assertTrue("chunk 2 is not marked as preview",chunk2.isPreviewChunk());
		assertEquals("chunk 2 content is wrong","s returned by an ori",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 3 was null",chunk3);
		assertTrue("chunk 3 is not marked as preview",chunk3.isPreviewChunk());
		assertEquals("chunk 3 content is wrong","g",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunkTrailer trailer = (IcapChunkTrailer)embedder.poll();
		assertNotNull("chunk trailer was null",trailer);
		assertTrue("trailer is not marked as preview",trailer.isPreviewChunk());
		assertTrue("trailer is not marked as early terminated",trailer.isEarlyTerminated());
	}
	
	@Test
	public void separateOPTIONSResponseWithBody() {
		embedder.offer(DataMockery.createOPTIONSResponseWithBodyInIcapResponse());
		IcapMessage message = (IcapMessage)embedder.poll();
		assertNotNull("message was null",message);
		assertEquals("message body indicator is wrong",IcapMessageElementEnum.OPTBODY,message.getBody());
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 1 was null",chunk1);
		assertEquals("chunk 1 content is wrong","This is data that wa",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 2 was null",chunk2);
		assertEquals("chunk 2 content is wrong","s returned by an ori",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 3 was null",chunk3);
		assertEquals("chunk 3 content is wrong","gin server.",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunkTrailer trailer = (IcapChunkTrailer)embedder.poll();
		assertNotNull("chunk trailer was null",trailer);
	}

	// TODO test options body
}
