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

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

public class EncapsulatedTest extends Assert {

	@Test
	public void testIcapMessageElementEnumNullValueHandling() {
		assertNull("value was not null as expected",IcapMessageElementEnum.fromString(null));
	}
	
	@Test
	public void testSimpleValueParsing() {
		String parameter = "req-hdr=0, res-hdr=45, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertEquals("wrong body element found",IcapMessageElementEnum.REQBODY,encapsulated.containsBodyEntry());
		assertTrue("req-hdr value is missing",encapsulated.containsEntry(IcapMessageElementEnum.REQHDR));
		assertTrue("res-hdr value is missing",encapsulated.containsEntry(IcapMessageElementEnum.RESHDR));
		assertTrue("req-body value is missing",encapsulated.containsEntry(IcapMessageElementEnum.REQBODY));
	}
	
	@Test
	public void testWhitespaceGap() {
		String parameter = "req-hdr=0,  res-hdr=45, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertEquals("wrong body element found",IcapMessageElementEnum.REQBODY,encapsulated.containsBodyEntry());
		assertTrue("req-hdr value is missing",encapsulated.containsEntry(IcapMessageElementEnum.REQHDR));
		assertTrue("res-hdr value is missing",encapsulated.containsEntry(IcapMessageElementEnum.RESHDR));
		assertTrue("req-body value is missing",encapsulated.containsEntry(IcapMessageElementEnum.REQBODY));
	}
	
	@Test
	public void testIteratorWithWrongSequence() {
		String parameter = "res-hdr=45, req-hdr=0, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertEquals("wrong body element found",IcapMessageElementEnum.REQBODY,encapsulated.containsBodyEntry());
		assertTrue("req-hdr value is missing",encapsulated.containsEntry(IcapMessageElementEnum.REQHDR));
		assertTrue("res-hdr value is missing",encapsulated.containsEntry(IcapMessageElementEnum.RESHDR));
		assertTrue("req-body value is missing",encapsulated.containsEntry(IcapMessageElementEnum.REQBODY));
		IcapMessageElementEnum reqhdr = encapsulated.getNextEntry();
		assertEquals("req-hdr was expected",IcapMessageElementEnum.REQHDR,reqhdr);
		encapsulated.setProcessed(reqhdr);
		IcapMessageElementEnum reshdr = encapsulated.getNextEntry();
		assertEquals("res-hdr was expected",IcapMessageElementEnum.RESHDR,reshdr);
		encapsulated.setProcessed(reshdr);
		IcapMessageElementEnum reqbody = encapsulated.getNextEntry();
		assertEquals("reqbody was expected",IcapMessageElementEnum.REQBODY,reqbody);
		encapsulated.setProcessed(reqbody);
		assertNull("null was expected after last entry is processed",encapsulated.getNextEntry());
	}
	
	@Test
	public void testGarbledString() {
		String parameter = "   req-hdr=0; req-body=124, Whaterver   ";
		boolean error = false;
		try {
			Encapsulated.parseHeader(parameter);
		} catch(Error e) {
			error = true;
		}
		assertTrue("Validation error did not occur",error);
	}
	
	@Test
	public void testEncodeEncapsulatedHeader() {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		Encapsulated encapsulated = new Encapsulated();
		encapsulated.addEntry(IcapMessageElementEnum.REQHDR,0);
		encapsulated.addEntry(IcapMessageElementEnum.RESHDR,123);
		encapsulated.addEntry(IcapMessageElementEnum.REQBODY,270);
		try {
			assertEquals("amount of bytes written was wrong",54,encapsulated.encode(buffer));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Encapsualted encoding failed");
		}
		assertEquals("encoded encapsulation header was wrong","Encapsulated: req-hdr=0, res-hdr=123, req-body=270\r\n\r\n",buffer.toString(Charset.defaultCharset()));
		assertEquals("wrong body element found",IcapMessageElementEnum.REQBODY,encapsulated.containsBodyEntry());
	}
	
	@Test
	public void testEncodeEncapsulatedHeaderWithOdering() {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		Encapsulated encapsulated = new Encapsulated();
		encapsulated.addEntry(IcapMessageElementEnum.REQBODY,270);
		encapsulated.addEntry(IcapMessageElementEnum.RESHDR,123);
		encapsulated.addEntry(IcapMessageElementEnum.REQHDR,0);

		try {
			assertEquals("amount of bytes written was wrong",54,encapsulated.encode(buffer));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Encapsualted encoding failed");
		}
		assertEquals("encoded encapsulation header was wrong","Encapsulated: req-hdr=0, res-hdr=123, req-body=270\r\n\r\n",buffer.toString(Charset.defaultCharset()));
		assertEquals("wrong body element found",IcapMessageElementEnum.REQBODY,encapsulated.containsBodyEntry());
	}
	
	@Test
	public void testEncodeEncapsulatedHeaderWithNullBody() {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		Encapsulated encapsulated = new Encapsulated();
		encapsulated.addEntry(IcapMessageElementEnum.NULLBODY,270);
		encapsulated.addEntry(IcapMessageElementEnum.RESHDR,123);
		encapsulated.addEntry(IcapMessageElementEnum.REQHDR,0);

		try {
			assertEquals("amount of bytes written was wrong",55,encapsulated.encode(buffer));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Encapsualted encoding failed");
		}
		assertEquals("encoded encapsulation header was wrong","Encapsulated: req-hdr=0, res-hdr=123, null-body=270\r\n\r\n",buffer.toString(Charset.defaultCharset()));
		assertEquals("wrong body element found",IcapMessageElementEnum.NULLBODY,encapsulated.containsBodyEntry());
	}
	
	@Test
	public void testEncodeEncapsulatedHeaderWithNullBodyThatHasZeroValue() {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		Encapsulated encapsulated = new Encapsulated();
		encapsulated.addEntry(IcapMessageElementEnum.NULLBODY,0);
		encapsulated.addEntry(IcapMessageElementEnum.RESHDR,123);
		encapsulated.addEntry(IcapMessageElementEnum.REQHDR,0);

		try {
			assertEquals("amount of bytes written was wrong",53,encapsulated.encode(buffer));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Encapsualted encoding failed");
		}
		assertEquals("encoded encapsulation header was wrong","Encapsulated: req-hdr=0, res-hdr=123, null-body=0\r\n\r\n",buffer.toString(Charset.defaultCharset()));
		assertEquals("wrong body element found",IcapMessageElementEnum.NULLBODY,encapsulated.containsBodyEntry());
	}
}
