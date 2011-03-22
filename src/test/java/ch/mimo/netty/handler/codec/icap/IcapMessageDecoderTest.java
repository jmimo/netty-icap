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

import java.util.Arrays;

import junit.framework.Assert;

import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.jboss.netty.util.internal.StringUtil;
import org.junit.Before;
import org.junit.Test;

public class IcapMessageDecoderTest extends Assert {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() {
		embedder = new DecoderEmbedder<Object>(new IcapRequestDecoder());
	}
	
	@Test
	public void decodeOPTIONRequestTest() {
		embedder.offer(DataMockery.createOPTIONSRequest());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateOPTIONSRequest(result);
	}
	
	@Test
	public void stripPrefixingWhitespacesFromMessage() {
		embedder.offer(DataMockery.createWhiteSpacePrefixedOPTIONSRequest());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateWhiteSpacePrefixedOPTIONSRequest(result);
	}
	
	@Test
	public void decodeREQMODRequestWithNullBody() {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBody() {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBodyAndReverseRequestAlignement() {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyAndReverseRequestAlignement());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBodyAndReverseRequestAlignement(result);
	}
	
	@Test
	public void decodeREQMODRequestWithTwoChunkBody() {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithTwoChunkBody(result);
		DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithTwoChunkBodyThirdChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeREQMODRequestWithPreview() {
		embedder.offer(DataMockery.createREQMODWithPreview());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithPreview(result);
		DataMockery.assertCreateREQMODWithPreviewChunk((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithPreviewChunkLastChunk((IcapChunk)embedder.poll());
	}
	
	@Test
	public void decodeREQMODRequestWithEarlyTerminatedPreview() {
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreview());
		IcapMessage result = (IcapMessage)embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithEarlyTerminatedPreview((IcapChunk)embedder.poll());
		DataMockery.assertCreateREQMODWithEarlyTerminatedPreviewLastChunk((IcapChunk)embedder.poll());
	}
	
//	@Test
//	public void testArrayEquals() {
//		Byte[] one = new Byte[]{11,34,105,28};
//		Object[] two = new Object[]{11,34,105,28};
//		Byte[] three = new Byte[]{11,34,105,28};
//		
//		assertTrue("arrays are not equals",Arrays.equals(one,three));
//	}
	
//	@Test
//	public void testPrintIEOF() {
//		System.out.print("[");
//		for(Byte bite : IcapCodecUtil.IEOF_SEQUENCE) {
//			System.out.print(new String(new byte[]{bite}));
//		}
//		System.out.println("]");
//	}
	
//	@Test
//	public void testNewLine() {
//		byte[] data = StringUtil.NEWLINE.getBytes();
//		for(Byte bite : data) {
//			System.out.print(Integer.toHexString(bite) + "|");
//		}
//		System.out.println("]");		
//	}
}
