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

import java.nio.charset.Charset;

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.internal.StringUtil;

public final class DataMockery extends Assert {

	private DataMockery() {

	}
	
	public static final ChannelBuffer createWhiteSpacePrefixedOPTIONSRequest() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"  OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap.google.com:1344");
		addLine(builder,"Encapsulated: null-body=0");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	public static final void assertCreateWhiteSpacePrefixedOPTIONSRequest(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap.google.com:1344",message);
		assertHeaderValue("Encapsulated","null-body=0",message);
	}
	
	public static final ChannelBuffer createOPTIONSRequest() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap.google.com:1344");
		addLine(builder,"Encapsulated: null-body=0");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	public static final void assertCreateOPTIONSRequest(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap.google.com:1344",message);
		assertHeaderValue("Encapsulated","null-body=0",message);
	}
	
	public static final ChannelBuffer createREQMODWithGetRequestNoBody() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap-server.net");
		addLine(builder,"Encapsulated: req-hdr=0, null-body=170");
		addLine(builder,null);
		addLine(builder,"GET / HTTP/1.1");
		addLine(builder,"Host: www.origin-server.com");
		addLine(builder,"Accept: text/html, text/plain");
		addLine(builder,"Accept-Encoding: compress");
		addLine(builder,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(builder,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	public static final void assertCreateREQMODWithGetRequestNoBody(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, null-body=170",message);
		assertNotNull("http request was null",message.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.GET,message.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","compress",message.getHttpRequest());
		assertHttpMessageHeaderValue("Cookie","ff39fk3jur@4ii0e02i",message.getHttpRequest());
		assertHttpMessageHeaderValue("If-None-Match","\"xyzzy\", \"r2d2xxxx\"",message.getHttpRequest());
	}
	
	public static final ChannelBuffer createRESPMODWithGetRequestNoBody() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap-server.net");
		addLine(builder,"Encapsulated: req-hdr=0, res-hdr=137, null-body=296");
		addLine(builder,null);
		addLine(builder,"GET /origin-resource HTTP/1.1");
		addLine(builder,"Host: www.origin-server.com");
		addLine(builder,"Accept: text/html, text/plain, image/gif");
		addLine(builder,"Accept-Encoding: gzip, compress");
		addLine(builder,null);
		addLine(builder,"HTTP/1.1 200 OK");
		addLine(builder,"Date: Mon, 10 Jan 2000 09:52:22 GMT");
		addLine(builder,"Server: Apache/1.3.6 (Unix)");
		addLine(builder,"ETag: \"63840-1ab7-378d415b\"");
		addLine(builder,"Content-Type: text/html");
		addLine(builder,"Content-Length: 51");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}	
	
	public static final void assertCreateRESPMODWithGetRequestNoBody(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, res-hdr=137, null-body=296",message);
		assertNotNull("http request was null",message.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.GET,message.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain, image/gif",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","gzip, compress",message.getHttpRequest());
		assertNotNull("http response was null",message.getHttpResponse());
		assertEquals("http response status was wrong",HttpResponseStatus.OK,message.getHttpResponse().getStatus());
		assertHttpMessageHeaderValue("Date","Mon, 10 Jan 2000 09:52:22 GMT",message.getHttpResponse());
		assertHttpMessageHeaderValue("Server","Apache/1.3.6 (Unix)",message.getHttpResponse());
		assertHttpMessageHeaderValue("ETag","\"63840-1ab7-378d415b\"",message.getHttpResponse());
		assertHttpMessageHeaderValue("Content-Type","text/html",message.getHttpResponse());
		assertHttpMessageHeaderValue("Content-Length","51",message.getHttpResponse());
	}
	
	
	public static final ChannelBuffer createRESPMODWithGetRequestNoBodyAndReverseRequestAlignement() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap-server.net");
		addLine(builder,"Encapsulated: res-hdr=0, req-hdr=137, null-body=296");
		addLine(builder,null);
		addLine(builder,"HTTP/1.1 200 OK");
		addLine(builder,"Date: Mon, 10 Jan 2000 09:52:22 GMT");
		addLine(builder,"Server: Apache/1.3.6 (Unix)");
		addLine(builder,"ETag: \"63840-1ab7-378d415b\"");
		addLine(builder,"Content-Type: text/html");
		addLine(builder,"Content-Length: 51");
		addLine(builder,null);
		addLine(builder,"GET /origin-resource HTTP/1.1");
		addLine(builder,"Host: www.origin-server.com");
		addLine(builder,"Accept: text/html, text/plain, image/gif");
		addLine(builder,"Accept-Encoding: gzip, compress");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}	
	
	public static final void assertCreateRESPMODWithGetRequestNoBodyAndReverseRequestAlignement(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","res-hdr=0, req-hdr=137, null-body=296",message);
		assertNotNull("http request was null",message.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.GET,message.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain, image/gif",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","gzip, compress",message.getHttpRequest());
		assertNotNull("http response was null",message.getHttpResponse());
		assertEquals("http response status was wrong",HttpResponseStatus.OK,message.getHttpResponse().getStatus());
		assertHttpMessageHeaderValue("Date","Mon, 10 Jan 2000 09:52:22 GMT",message.getHttpResponse());
		assertHttpMessageHeaderValue("Server","Apache/1.3.6 (Unix)",message.getHttpResponse());
		assertHttpMessageHeaderValue("ETag","\"63840-1ab7-378d415b\"",message.getHttpResponse());
		assertHttpMessageHeaderValue("Content-Type","text/html",message.getHttpResponse());
		assertHttpMessageHeaderValue("Content-Length","51",message.getHttpResponse());
	}
	
	
	public static final ChannelBuffer createREQMODWithTwoChunkBody() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap-server.net");
		addLine(builder,"Encapsulated: req-hdr=0, req-body=170");
		addLine(builder,null);
		addLine(builder,"POST / HTTP/1.1");
		addLine(builder,"Host: www.origin-server.com");
		addLine(builder,"Accept: text/html, text/plain");
		addLine(builder,"Accept-Encoding: compress");
		addLine(builder,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(builder,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(builder,null);
		addChunk(builder,"This is data that was returned by an origin server.");
		addChunk(builder,"And this the second chunk which contains more information.");
		addChunk(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	public static final void assertCreateREQMODWithTwoChunkBody(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=170",message);
		assertNotNull("http request was null",message.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.POST,message.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","compress",message.getHttpRequest());
		assertHttpMessageHeaderValue("Cookie","ff39fk3jur@4ii0e02i",message.getHttpRequest());
		assertHttpMessageHeaderValue("If-None-Match","\"xyzzy\", \"r2d2xxxx\"",message.getHttpRequest());
	}
	
	public static final void assertCreateREQMODWithTwoChunkBodyFirstChunk(IcapChunk chunk) {
		assertChunk("first chunk",chunk,"This is data that was returned by an origin server.",false);
	}
	
	public static final void assertCreateREQMODWithTwoChunkBodySecondChunk(IcapChunk chunk) {
		assertChunk("second chunk",chunk,"And this the second chunk which contains more information.",false);
	}
	
	public static final void assertCreateREQMODWithTwoChunkBodyThirdChunk(IcapChunk chunk) {
		assertChunk("third chunk",chunk,null,true);
	}
	
	public static final ChannelBuffer createREQMODWithPreview() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap-server.net");
		addLine(builder,"Encapsulated: req-hdr=0, req-body=181");
		addLine(builder,"Preview: 51");
		addLine(builder,null);
		addLine(builder,"POST / HTTP/1.1");
		addLine(builder,"Host: www.origin-server.com");
		addLine(builder,"Accept: text/html, text/plain");
		addLine(builder,"Accept-Encoding: compress");
		addLine(builder,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(builder,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(builder,null);
		addChunk(builder,"This is data that was returned by an origin server.");
		addChunk(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	public static final void assertCreateREQMODWithPreview(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=181",message);
		assertHeaderValue("Preview","51",message);
		assertNotNull("http request was null",message.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.POST,message.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","compress",message.getHttpRequest());
		assertHttpMessageHeaderValue("Cookie","ff39fk3jur@4ii0e02i",message.getHttpRequest());
		assertHttpMessageHeaderValue("If-None-Match","\"xyzzy\", \"r2d2xxxx\"",message.getHttpRequest());
	}
	
	public static final void assertCreateREQMODWithPreviewChunk(IcapChunk chunk) {
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertFalse("preview chunk indicated that is is early terminated",chunk.isEarlyTerminated());
		assertChunk("preview chunk", chunk,"This is data that was returned by an origin server.",false);
	}
	
	public static final void assertCreateREQMODWithPreviewChunkLastChunk(IcapChunk chunk) {
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertTrue("preview chunk is not last chunk",chunk.isLast());
		assertFalse("preview chunk states that it is early terminated",chunk.isEarlyTerminated());
	}
	
	public static final ChannelBuffer createREQMODWithEarlyTerminatedPreview() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap-server.net");
		addLine(builder,"Encapsulated: req-hdr=0, req-body=181");
		addLine(builder,"Preview: 51");
		addLine(builder,null);
		addLine(builder,"POST / HTTP/1.1");
		addLine(builder,"Host: www.origin-server.com");
		addLine(builder,"Accept: text/html, text/plain");
		addLine(builder,"Accept-Encoding: compress");
		addLine(builder,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(builder,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(builder,null);
		addLine(builder,"33");
		addLine(builder,"This is data that was returned by an origin");
//		builder.append("0; ieof").append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF).append((char)IcapCodecUtil.CR).append((char)IcapCodecUtil.LF);
		addLine(builder,"0; ieof");
		addLine(builder,null);
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	public static final void assertCreateREQMODWithEarlyTerminatedPreview(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=181",message);
		assertHeaderValue("Preview","51",message);
		assertNotNull("http request was null",message.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.POST,message.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","compress",message.getHttpRequest());
		assertHttpMessageHeaderValue("Cookie","ff39fk3jur@4ii0e02i",message.getHttpRequest());
		assertHttpMessageHeaderValue("If-None-Match","\"xyzzy\", \"r2d2xxxx\"",message.getHttpRequest());
	}
	
	public static final void assertCreateREQMODWithEarlyTerminatedPreview(IcapChunk chunk) {
		assertNotNull("preview chunk was null",chunk);
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertTrue("preview chunk does not indicated that is is early terminated",chunk.isEarlyTerminated());
		assertChunk("preview chunk", chunk,"This is data that was returned by an origin",false);
	}
	
	public static final void assertCreateREQMODWithEarlyTerminatedPreviewLastChunk(IcapChunk chunk) {
		assertNotNull("preview last chunk was null",chunk);
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertTrue("preview chunk is not last chunk",chunk.isLast());
		assertTrue("preview chunk is not early terminated",chunk.isEarlyTerminated());
	}
	
//	public static final ChannelBuffer createRESPMODWithGetRequestAndBody() {
//		StringBuilder builder = new StringBuilder();
//		addLine(builder,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
//		addLine(builder,"Host: icap-server.net");
//		addLine(builder,"Encapsulated: req-hdr=0, res-hdr=137, res-body=296");
//		addLine(builder,null);
//		addLine(builder,"GET /origin-resource HTTP/1.1");
//		addLine(builder,"Host: www.origin-server.com");
//		addLine(builder,"Accept: text/html, text/plain, image/gif");
//		addLine(builder,"Accept-Encoding: gzip, compress");
//		addLine(builder,null);
//		addLine(builder,"HTTP/1.1 200 OK");
//		addLine(builder,"Date: Mon, 10 Jan 2000 09:52:22 GMT");
//		addLine(builder,"Server: Apache/1.3.6 (Unix)");
//		addLine(builder,"ETag: \"63840-1ab7-378d415b\"");
//		addLine(builder,"Content-Type: text/html");
//		addLine(builder,"Content-Length: 51");
//		addLine(builder,null);
//		addLine(builder,"33");
//		addLine(builder,"This is data that was returned by an origin server.");
//		addLine(builder,"0");
//		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
//	}
	
	private static final void addLine(StringBuilder builder, String value) {
		if(value == null) {
			builder.append(StringUtil.NEWLINE);
		} else {
			builder.append(value).append(StringUtil.NEWLINE);
		}
	}
	
	private static void addChunk(StringBuilder builder, String chunkData) {
		if(chunkData == null) {
			addLine(builder,"0");
			addLine(builder,null);
			addLine(builder,null);
		} else {
			int length = chunkData.length();
			String hex = Integer.toString(length,16);
			addLine(builder,hex);
			addLine(builder,chunkData);
		}
	}
	
	private static void assertHeaderValue(String key, String expected, IcapMessage message) {
		assertNotNull("Message was null",message);
		assertTrue("Key does not exist [" + key + "]",message.containsHeader(key));
		assertEquals("The header: " + key + " is invalid",expected,message.getHeader(key));
	}
	
	private static void assertHttpMessageHeaderValue(String key, String expected, HttpMessage message) {
		assertNotNull("Message was null",message);
		assertTrue("Key does not exist [" + key + "]",message.containsHeader(key));
		assertEquals("The header: " + key + " is invalid",expected,message.getHeader(key));
	}
	
	private static void assertChunk(String title, IcapChunk chunk, String expectedContent, boolean isLast) {
		assertNotNull(title + " chunk is null",chunk);
		if(isLast) {
			assertTrue(title + " is not last chunk",chunk.isLast());
		} else {
			ChannelBuffer buffer = chunk.getContent();
			assertNotNull(title + " chunk buffer is null",buffer);
			assertFalse(title + " chunk buffer is empty",buffer == ChannelBuffers.EMPTY_BUFFER);
			String bufferContent = buffer.toString(Charset.defaultCharset());
			assertEquals(title + " chunk content was wrong",expectedContent,bufferContent);
		}
	}
}
