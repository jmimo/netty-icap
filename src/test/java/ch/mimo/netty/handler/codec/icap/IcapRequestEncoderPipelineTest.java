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
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapRequestEncoderPipelineTest extends AbstractIcapTest {

	private EncoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new EncoderEmbedder<Object>(new IcapRequestEncoder(),new IcapChunkSeparator(4012));
	}
	
	@Test
	public void encodeREQMODRequestWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyIcapMessage());
		ChannelBuffer buffer = (ChannelBuffer)embedder.poll();
		assertNotNull("buffer was null",buffer);
		assertEquals("buffer content is wrong",DataMockery.createREQMODWithGetRequestNoBody().toString(Charset.defaultCharset()),buffer.toString(Charset.defaultCharset()));
	}
	
	@Test
	public void encodeREQMODRequestWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestAndDataIcapMessage());
		ChannelBuffer buffer = (ChannelBuffer)embedder.poll();
		assertNotNull("message buffer was null",buffer);
		String message = buffer.toString(Charset.defaultCharset());
		assertEquals("message buffer content is wrong",DataMockery.createREQMODWithGetRequestAndData().toString(Charset.defaultCharset()),message);
		buffer = (ChannelBuffer)embedder.poll();
		assertNotNull("chnunk buffer was null",buffer);
		message = buffer.toString(Charset.defaultCharset());
		assertEquals("chunk buffer content is wrong",DataMockery.createREQMODWithGetRequestAndDataFirstChunk().toString(Charset.defaultCharset()),message);
		buffer = (ChannelBuffer)embedder.poll();
		assertNotNull("last chnunk buffer was null",buffer);
		message = buffer.toString(Charset.defaultCharset());
		assertEquals("last chunk is wrong",DataMockery.createREQMODWithGetRequestAndDataLastChunk().toString(Charset.defaultCharset()),message);
		assertNull("poll is not empty",embedder.poll());
	}
}
