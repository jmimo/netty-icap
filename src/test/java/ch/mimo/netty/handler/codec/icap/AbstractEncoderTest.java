/*******************************************************************************
 * Copyright (c) 2012 Michael Mimo Moratti.
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

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractEncoderTest extends AbstractIcapTest {

	private StringBuilder outputBuilder;
	
	@Before
	public void prepareBuilder() {
		outputBuilder = new StringBuilder();
	}
	
	@After
	public void printBuilderOutput() {
		if(outputBuilder != null) {
			doOutput(outputBuilder.toString());
		}
	}
	
	protected String getBufferContent(Object object) {
		assertNotNull("poll returned null",object);
		assertTrue("returned object from embedder is not of type ChannelBuffer",object instanceof ChannelBuffer);
		ChannelBuffer buffer = (ChannelBuffer)object;
		return buffer.toString(Charset.defaultCharset());
	}
	
	protected void assertResponse(ChannelBuffer expected, String request) {
		String content = getBufferContent(expected);
		outputBuilder.append(content);
		assertEquals("encoded request is wrong",content,request);
	}
}
