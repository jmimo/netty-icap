/*******************************************************************************
 * Copyright 2012 Michael Mimo Moratti
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

import java.util.List;

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

public class IcapDecoderUtilTest extends Assert {

	@Test
	public void testControlCharacterSkipping() {
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer("  TESTLINE".getBytes());
		IcapDecoderUtil.skipControlCharacters(buffer);
		assertEquals("skipping of whitespaces has not worked",'T',(char)buffer.readByte());
	}
	
	@Test
	public void testSkipControlCharacter() {
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(new byte[]{0x0001,'A'});
		IcapDecoderUtil.skipControlCharacters(buffer);
		assertEquals("control character was not skiped",'A',buffer.readByte());
	}
	
	@Test
	public void testReadLine() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(IcapCodecUtil.CRLF)).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		String line = null;
		try {
			line = IcapDecoderUtil.readLine(buffer,100);
		} catch (DecodingException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",line);
	}
	
	@Test
	public void testReadLineWithSingleLineBreak() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(new byte[]{IcapCodecUtil.LF})).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		String line = null;
		try {
			line = IcapDecoderUtil.readLine(buffer,100);
		} catch (DecodingException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",line);
	}
	
	@Test
	public void testReadLineMaximumLengthReached() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(IcapCodecUtil.CRLF)).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		boolean exception = false;
		try {
			IcapDecoderUtil.readLine(buffer,42);
		} catch (DecodingException e) {
			exception = true;
		}
		assertTrue("No maximum length reached exception was thrown!",exception);
	}
	
	@Test
	public void testPreviewLine() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(IcapCodecUtil.CRLF)).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		try {
			String line = IcapDecoderUtil.previewLine(buffer,100);
			assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",line);
			String secondLine = IcapDecoderUtil.previewLine(buffer,100);
			assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",secondLine);
			String readLine = IcapDecoderUtil.readLine(buffer,100);
			assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",readLine);
		} catch (DecodingException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testPreviewLineWithSingleLineBreak() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(new byte[]{IcapCodecUtil.LF})).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		try {
			String line = IcapDecoderUtil.previewLine(buffer,100);
			assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",line);
			String secondLine = IcapDecoderUtil.previewLine(buffer,100);
			assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",secondLine);
			String readLine = IcapDecoderUtil.readLine(buffer,100);
			assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",readLine);
		} catch (DecodingException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testPreviewLineMaximumLengthReached() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(IcapCodecUtil.CRLF)).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		boolean exception = false;
		try {
			IcapDecoderUtil.previewLine(buffer,42);
		} catch (DecodingException e) {
			exception = true;
		}
		assertTrue("No maximum length reached exception was thrown!",exception);
	}
	
	@Test
	public void testFindNonWhitespace() {
		String line = "  TESTSTRING";
		int position = IcapDecoderUtil.findNonWhitespace(line,0);
		assertEquals("Position of first non whitespace is wrong",2,position);
	}
	
	@Test
	public void testFindFirstWhitespace() {
		String line = "TESTSTRING1 TESTSTRING2";
		int position = IcapDecoderUtil.findWhitespace(line,0);
		assertEquals("Position of first whitespace is wrong",11,position);
	}
	
	@Test
	public void testFindEndOfString() {
		StringBuilder builder = new StringBuilder("TESTSTRING1 TESTSTRING2").append("    ");
		int end = IcapDecoderUtil.findEndOfString(builder.toString());
		assertEquals("End of String position is wrong",23,end);
	}
	
	@Test
	public void testParseInitialLine() {
		String initialLine = "REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0";
		String[] params = IcapDecoderUtil.splitInitialLine(initialLine);
		assertEquals("Operation could not be identified","REQMOD",params[0]);
		assertEquals("Uri could not be identified","icap://icap.mimo.ch:1344/reqmod",params[1]);
		assertEquals("Protocol and Version could not be identified","ICAP/1.0",params[2]);
	}
	
	@Test
	public void testParseInitialLineWithTwoElements() {
		String initialLine = "HTTP/1.1 200";
		String[] params = IcapDecoderUtil.splitInitialLine(initialLine);
		assertEquals("Protocol and Version could not be identified","HTTP/1.1",params[0]);
		assertEquals("Response status could not be identified","200",params[1]);
	}
	
	@Test
	public void testParseInitialLineWithTrailingWhitespaces() {
		String initialLine = "  REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0  ";
		String[] params = IcapDecoderUtil.splitInitialLine(initialLine);
		assertEquals("Operation could not be identified","REQMOD",params[0]);
		assertEquals("Uri could not be identified","icap://icap.mimo.ch:1344/reqmod",params[1]);
		assertEquals("Protocol and Version could not be identified","ICAP/1.0",params[2]);
	}
	
	@Test
	public void testParseInitialLineWithFourElements() {
		String initialLine = "REQMOD icap://icap.mimo.ch:1344/reqmod Bla ICAP/1.0";
		String[] params = IcapDecoderUtil.splitInitialLine(initialLine);
		assertEquals("Operation could not be identified","REQMOD",params[0]);
		assertEquals("Uri could not be identified","icap://icap.mimo.ch:1344/reqmod",params[1]);
		assertEquals("Protocol and Version could not be identified","Bla ICAP/1.0",params[2]);
	}
	
	@Test
	public void testParseInitialLineWithFourtElementAtEndOfLine() {
		String initialLine = "REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0 Bla";
		String[] params = IcapDecoderUtil.splitInitialLine(initialLine);
		assertEquals("Operation could not be identified","REQMOD",params[0]);
		assertEquals("Uri could not be identified","icap://icap.mimo.ch:1344/reqmod",params[1]);
		assertEquals("Protocol and Version could not be identified","ICAP/1.0 Bla",params[2]);
	}
	
	@Test
	public void testParseHeaderLine() {
		StringBuilder builder = new StringBuilder("Encapsulation: req-hdr=50, res-hdr=120, null-body=210");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append("Host: icap.mimo.ch");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		String header = null;
		try {
			SizeDelimiter sizeDelimiter = new SizeDelimiter(200);
			header = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
			assertEquals("Encapsulation header was expected","Encapsulation: req-hdr=50, res-hdr=120, null-body=210",header);
			header = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
			assertEquals("Host header was expected","Host: icap.mimo.ch",header);
			assertEquals("total length of parsed headers is wrong",75,sizeDelimiter.getSize());
		} catch (DecodingException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testParseTooLargeHeaderLine() {
		StringBuilder builder = new StringBuilder("Encapsulation: req-hdr=50, res-hdr=120, null-body=210");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append("Host: icap.mimo.ch");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		boolean exception = false;
		try {
			SizeDelimiter sizeDelimiter = new SizeDelimiter(40);
			IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
		} catch (DecodingException e) {
			exception = true;
		}
		assertTrue("No exception was thrown",exception);
	}
	
	@Test
	public void testEndOfHeaders() {
		StringBuilder builder = new StringBuilder("Encapsulation: req-hdr=50, res-hdr=120, null-body=210");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append("Host: icap.mimo.ch");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append("GET / HTTP/1.1");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		String header = null;
		try {
			SizeDelimiter sizeDelimiter = new SizeDelimiter(200);
			header = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
			assertEquals("Encapsulation header was expected","Encapsulation: req-hdr=50, res-hdr=120, null-body=210",header);
			header = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
			assertEquals("Host header was expected","Host: icap.mimo.ch",header);
			assertEquals("total length of parsed headers is wrong",75,sizeDelimiter.getSize());
			header = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
			assertEquals("header length is not null",0,header.length());
			byte bite = buffer.getByte(buffer.readerIndex());
			assertEquals("...",'G',(char)bite);
		} catch (DecodingException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testHeaderSplit() {
		String header = "Encapsulation: req-hdr=50, res-hdr=120, null-body=210";
		String[] elements = IcapDecoderUtil.splitHeader(header);
		assertNotNull("header elemens are null",elements);
		assertEquals("Header Key was not expected","Encapsulation",elements[0]);
		assertEquals("Header Value was not expected","req-hdr=50, res-hdr=120, null-body=210",elements[1]);
	}
	
	@Test
	public void testHeaderSplitWithWhitespace() {
		String header = "Encapsulation";
		String[] elements = IcapDecoderUtil.splitHeader(header);
		assertNotNull("header elemens are null",elements);
		assertEquals("Header Key was not expected","Encapsulation",elements[0]);
		assertEquals("Header Value was not expected","",elements[1]);
	}
	
	@Test
	public void testHeaderSplitWithSemicolon() throws DecodingException {
		StringBuilder builder = new StringBuilder("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append("Mimo: ;:");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		List<String[]> headers = IcapDecoderUtil.readHeaders(buffer,400);
		Assert.assertEquals("wrong amount of header entries in list",2,headers.size());
		String[] header1 = headers.get(0);
		Assert.assertEquals("Wrong header name","Accept",header1[0]);
		Assert.assertEquals("Wrong header value","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",header1[1]);
		String[] header2 = headers.get(1);
		Assert.assertEquals("Wrong header name","Mimo",header2[0]);
		Assert.assertEquals("Wrong header value",";:",header2[1]);
	}
	
// TODO ;: header issue from "Michal Przytulski" I could imagine be lenient about it......
	// But I don't really fancy to solve such an issue since such a header really breaks the protocol. 
	/*
< HTTP/1.0 200 OK
< Server: nginx
< Date: Mon, 26 Mar 2012 13:42:17 GMT
< Content-Type: text/javascript; charset=
< Pragma: no-cache
< ;: 
< X-Cache: MISS from chzhuspfw-havp.united-security-providers.ch
< X-Cache-Lookup: MISS from chzhuspfw-havp.united-security-providers.ch:8081
< X-Cache: MISS from chzhuspfw01
< X-Cache-Lookup: MISS from chzhuspfw01:8080
< Via: 1.0 chzhuspfw-havp.united-security-providers.ch (squid/3.1.18), 1.0 chzhuspfw01 (squid/3.1.18)
< Connection: close
< 
	 */
	
	@Test
	public void testNonSimpleHeader() {
		StringBuilder builder = new StringBuilder("Encapsulation: req-hdr=50, res-hdr=120, null-body=210");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append("Host: icap.mimo.ch");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append("NonSimpleHeader: NonSimpleValue1");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append(" NonSimpleValue2");
		builder.append("\t").append("NonSimpleValue3");
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		builder.append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		try {
			List<String[]> headers = IcapDecoderUtil.readHeaders(buffer,400);
			String[] header1 = headers.get(0);
			assertEquals("header name was wrong",header1[0],"Encapsulation");
			assertEquals("header value was wrong",header1[1],"req-hdr=50, res-hdr=120, null-body=210");
			String[] header2 = headers.get(1);
			assertEquals("header name was wrong",header2[0],"Host");
			assertEquals("header value was wrong",header2[1],"icap.mimo.ch");
			String[] header3 = headers.get(2);
			assertEquals("header name was wrong",header3[0],"NonSimpleHeader");
			StringBuilder resultBuilder = new StringBuilder("NonSimpleValue1 NonSimpleValue2");
			resultBuilder.append("\t").append("NonSimpleValue3");
			assertEquals("header value was wrong",header3[1],resultBuilder.toString());
		
		} catch(DecodingException tlfe) {
			fail("unexpected frame exception");
		}
	}
	
	@Test
	public void testChunkSize() throws DecodingException {
		String line = "33";
		assertEquals("wrong chunk size",51,IcapDecoderUtil.getChunkSize(line));
	}
	
	@Test
	public void testIEOFChunkSize() throws DecodingException {
		String line ="0; ieof";
		assertEquals("wrong chunk size",-1,IcapDecoderUtil.getChunkSize(line));
	}
	
	@Test
	public void testChunkSizeWithTrailingSemicolon() throws DecodingException {
		String line = "0;";
		assertEquals("wrong chunk size",0,IcapDecoderUtil.getChunkSize(line));
	}
	
	@Test
	public void testChunkSizeWithTrailingWhitespace() throws DecodingException {
		String line = "0 ";
		assertEquals("wrong chunk size",0,IcapDecoderUtil.getChunkSize(line));
	}
	
	@Test
	public void testChunkSizeWithUnparseableValues() {
		boolean exception = false;
		try {
			IcapDecoderUtil.getChunkSize("HELLO");
		} catch(DecodingException de) {
			exception = true;
		}
		assertTrue("No exception was thrown",exception);
	}
}
