package ch.mimo.netty.handler.codec.icap;

import java.nio.charset.Charset;

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpChunk;
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
		addLine(builder,"33");
		addLine(builder,"This is data that was returned by an origin server.");
		addLine(builder,"3A");
		addLine(builder,"And this the second chunk which contains more information.");		
		addLine(builder,"0");
		addLine(builder,null);
		addLine(builder,null);
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
	
	public static final void assertCreateREQMODWithTwoChunkBodyFirstChunk(HttpChunk chunk) {
		assertChunk("first chunk",chunk,"This is data that was returned by an origin server.",false);
	}
	
	public static final void assertCreateREQMODWithTwoChunkBodySecondChunk(HttpChunk chunk) {
		assertChunk("second chunk",chunk,"And this the second chunk which contains more information.",false);
	}
	
	public static final void assertCreateREQMODWithTwoChunkBodyThirdChunk(HttpChunk chunk) {
		assertChunk("third chunk",chunk,null,true);
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
	
	private static void assertChunk(String title, HttpChunk chunk, String expectedContent, boolean isLast) {
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
