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
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public final class DataMockery extends Assert {

	private DataMockery() {

	}
	
	public static final ChannelBuffer createWhiteSpacePrefixedOPTIONSRequest() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"  OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap.google.com:1344");
		addLine(buffer,"Encapsulated: null-body=0");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final void assertCreateWhiteSpacePrefixedOPTIONSRequest(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap.google.com:1344",message);
		assertHeaderValue("Encapsulated","null-body=0",message);
	}
	
	public static final ChannelBuffer createOPTIONSRequest() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap.google.com:1344");
		addLine(buffer,"Encapsulated: null-body=0");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final IcapMessage createOPTIONSRequestIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.OPTIONS,"icap://icap.mimo.ch:1344/reqmod");
		request.addHeader("Host","icap.google.com:1344");
		return request;
	}
	
	public static final void assertCreateOPTIONSRequest(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap.google.com:1344",message);
		assertHeaderValue("Encapsulated","null-body=0",message);
	}
	
	public static final ChannelBuffer createOPTIONSRequestWithBody() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap.google.com:1344");
		addLine(buffer,"Encapsulated: opt-body=0");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final void assertOPTIONSRequestWithBody(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertEquals("wrong request type",IcapMethod.OPTIONS,message.getMethod());
		assertHeaderValue("Host","icap.google.com:1344",message);
		assertHeaderValue("Encapsulated","opt-body=0",message);
	}
	
	public static final IcapMessage createOPTIONSRequestWithBodyIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.OPTIONS,"icap://icap.mimo.ch:1344/reqmod");
		request.addHeader("Host","icap.google.com:1344");
		request.setBody(EntryName.OPTBODY);
		return request;
	}
	
	public static final ChannelBuffer createOPTIONSRequestWithBodyBodyChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,"This is a options body chunk.");
		return buffer;
	}
	
	public static final IcapChunk createOPTIONSRequestWithBodyBodyChunkIcapChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is a options body chunk.".getBytes("ASCII"));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		return chunk;
	}
	
	public static void assertOPTIONSRequestWithBodyBodyChunk(IcapChunk chunk) {
		assertChunk("options body chunk",chunk,"This is a options body chunk.",false);
	}
	
	public static final ChannelBuffer createOPTIONSRequestWithBodyLastChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,null);
		return buffer;
	}
	
	public static final IcapChunk createOPTIONSRequestWithBodyLastChunkIcapChunk() throws UnsupportedEncodingException {
		IcapChunk chunk = new DefaultIcapChunk(ChannelBuffers.EMPTY_BUFFER);
		return chunk;
	}
	
	public static final void assertOPTIONSRequestWithBodyLastChunk(IcapChunk chunk) {
		assertChunk("options last chunk",chunk,null,true);
	}
	
	public static final ChannelBuffer createREQMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, null-body=168");
		addLine(buffer,null);
		addLine(buffer,"GET / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final IcapRequest createREQMODWithGetRequestNoBodyIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod");
		request.addHeader("Host","icap-server.net");
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return request;
	}
	
	public static final void assertCreateREQMODWithGetRequestNoBody(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, null-body=168",message);
		assertNotNull("http request was null",message.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.GET,message.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain",message.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","compress",message.getHttpRequest());
		assertHttpMessageHeaderValue("Cookie","ff39fk3jur@4ii0e02i",message.getHttpRequest());
		assertHttpMessageHeaderValue("If-None-Match","\"xyzzy\", \"r2d2xxxx\"",message.getHttpRequest());
	}
	
	public static final ChannelBuffer createRESPMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, res-hdr=135, null-body=292");
		addLine(buffer,null);
		addLine(buffer,"GET /origin-resource HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain, image/gif");
		addLine(buffer,"Accept-Encoding: gzip, compress");
		addLine(buffer,null);
		addLine(buffer,"HTTP/1.1 200 OK");
		addLine(buffer,"Date: Mon, 10 Jan 2000 09:52:22 GMT");
		addLine(buffer,"Server: Apache/1.3.6 (Unix)");
		addLine(buffer,"ETag: \"63840-1ab7-378d415b\"");
		addLine(buffer,"Content-Type: text/html");
		addLine(buffer,"Content-Length: 51");
		addLine(buffer,null);
		return buffer;
	}	
	
	public static final IcapMessage createRESPMODWithGetRequestNoBodyIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.RESPMOD,"icap://icap.mimo.ch:1344/reqmod");
		request.addHeader("Host","icap-server.net");
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/origin-resource");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain, image/gif");
		httpRequest.addHeader("Accept-Encoding","gzip, compress");
		HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
		request.setHttpResponse(httpResponse);
		httpResponse.addHeader("Date","Mon, 10 Jan 2000 09:52:22 GMT");
		httpResponse.addHeader("Server","Apache/1.3.6 (Unix)");
		httpResponse.addHeader("ETag","\"63840-1ab7-378d415b\"");
		httpResponse.addHeader("Content-Type","text/html");
		httpResponse.addHeader("Content-Length","51");
		return request;
	}
	
	public static final void assertCreateRESPMODWithGetRequestNoBody(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, res-hdr=135, null-body=292",message);
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
	
	
	public static final ChannelBuffer createRESPMODWithGetRequestNoBodyAndReverseRequestAlignement() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: res-hdr=0, req-hdr=137, null-body=296");
		addLine(buffer,null);
		addLine(buffer,"HTTP/1.1 200 OK");
		addLine(buffer,"Date: Mon, 10 Jan 2000 09:52:22 GMT");
		addLine(buffer,"Server: Apache/1.3.6 (Unix)");
		addLine(buffer,"ETag: \"63840-1ab7-378d415b\"");
		addLine(buffer,"Content-Type: text/html");
		addLine(buffer,"Content-Length: 51");
		addLine(buffer,null);
		addLine(buffer,"GET /origin-resource HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain, image/gif");
		addLine(buffer,"Accept-Encoding: gzip, compress");
		addLine(buffer,null);
		return buffer;
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
	
	
	public static final ChannelBuffer createREQMODWithTwoChunkBody() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=169");
		addLine(buffer,null);
		addLine(buffer,"POST / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		addChunk(buffer,"This is data that was returned by an origin server.");
		addChunk(buffer,"And this the second chunk which contains more information.");
		addChunk(buffer,null);
		return buffer;
	}
	
	public static final ChannelBuffer createREQMODWithTwoChunkBodyAnnouncement() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=169");
		addLine(buffer,null);
		addLine(buffer,"POST / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final IcapMessage createREQMODWithTwoChunkBodyIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod");
		request.addHeader("Host","icap-server.net");
		request.setBody(EntryName.REQBODY);
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return request;
	}
	
	public static final IcapChunk createREQMODWithTwoChunkBodyIcapChunkOne() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is data that was returned by an origin server.".getBytes("ASCII"));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		return chunk;
	}
	
	public static final ChannelBuffer createREQMODWithTowChunkBodyChunkOne() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,"This is data that was returned by an origin server.");
		return buffer;
	}
	
	public static final IcapChunk createREQMODWithTwoChunkBodyIcapChunkTwo() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("And this the second chunk which contains more information.".getBytes("ASCII"));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		return chunk;
	}
	
	public static final ChannelBuffer createREQMODWithTwoChunkBodyChunkTwo() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,"And this the second chunk which contains more information.");
		return buffer;
	}
	
	public static final IcapChunk createREQMODWithTwoChunkBodyIcapChunkThree() throws UnsupportedEncodingException {
		IcapChunk chunk = new DefaultIcapChunk(ChannelBuffers.EMPTY_BUFFER);
		return chunk;
	}
	
	public static final ChannelBuffer createREQMODWithTwoChunkBodyChunkThree() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,null);
		return buffer;
	}
	
	public static final IcapChunkTrailer createREQMODWithTwoChunkBodyChunkThreeIcapChunkTrailer() throws UnsupportedEncodingException {
		IcapChunkTrailer trailer = new DefaultIcapChunkTrailer();
		trailer.addHeader("TrailingHeaderKey1","TrailingHeaderValue1");
		trailer.addHeader("TrailingHeaderKey2","TrailingHeaderValue2");
		trailer.addHeader("TrailingHeaderKey3","TrailingHeaderValue3");
		trailer.addHeader("TrailingHeaderKey4","TrailingHeaderValue4");
		return trailer;
	}
	
	public static final ChannelBuffer createREQMODWithTwoChunkBodyChunkThreeWithTrailer() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"0");
		addLine(buffer,"TrailingHeaderKey1: TrailingHeaderValue1");
		addLine(buffer,"TrailingHeaderKey2: TrailingHeaderValue2");
		addLine(buffer,"TrailingHeaderKey3: TrailingHeaderValue3");
		addLine(buffer,"TrailingHeaderKey4: TrailingHeaderValue4");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final ChannelBuffer createREQMODWithTwoChunkBodyAndTrailingHeaders() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=169");
		addLine(buffer,null);
		addLine(buffer,"POST / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		addChunk(buffer,"This is data that was returned by an origin server.");
		addChunk(buffer,"And this the second chunk which contains more information.");
		addLine(buffer,"0");
		addLine(buffer,"TrailingHeaderKey1: TrailingHeaderValue1");
		addLine(buffer,"TrailingHeaderKey2: TrailingHeaderValue2");
		addLine(buffer,"TrailingHeaderKey3: TrailingHeaderValue3");
		addLine(buffer,"TrailingHeaderKey4: TrailingHeaderValue4");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final void assertCreateREQMODWithTwoChunkBody(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=169",message);
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
	
	public static final void assertCreateREQMODWithTwoChunkBodyTrailingHeaderChunk(IcapChunkTrailer trailer) {
		assertNotNull("trailer is null",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey1","TrailingHeaderValue1",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey2","TrailingHeaderValue2",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey3","TrailingHeaderValue3",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey4","TrailingHeaderValue4",trailer);
	}
	
	public static final ChannelBuffer createREQMODWithPreview() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Preview: 51");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=181");
		addLine(buffer,null);
		addLine(buffer,"POST / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		addChunk(buffer,"This is data that was returned by an origin server.");
		addChunk(buffer,null);
		return buffer;
	}
	
	public static final ChannelBuffer createREQMODWithPreviewAnnouncement() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Preview: 51");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=169");
		addLine(buffer,null);
		addLine(buffer,"POST / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final IcapMessage createREQMODWithPreviewAnnouncementIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod");
		request.addHeader("Host","icap-server.net");
		request.addHeader("Preview","51");
		request.setBody(EntryName.REQBODY);
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return request;
	}
	
	public static final IcapChunk createREQMODWithPreviewIcapChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is data that was returned by an origin server.".getBytes("ASCII"));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		chunk.setIsPreviewChunk();
		return chunk;
	}
	
	public static final ChannelBuffer createREQMODWithPreviewChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,"This is data that was returned by an origin server.");
		return buffer;
	}
	
	public static final IcapChunk createREQMODWithPreviewLastIcapChunk() throws UnsupportedEncodingException {
		IcapChunk chunk = new DefaultIcapChunk(ChannelBuffers.EMPTY_BUFFER);
		chunk.setIsPreviewChunk();
		return chunk;
	}
	
	public static final ChannelBuffer createREQMODWithPreviewLastChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,null);
		return buffer;
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
	
	public static final ChannelBuffer createREQMODWithEarlyTerminatedPreview() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Preview: 151");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=169");
		addLine(buffer,null);
		addLine(buffer,"POST / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		addLine(buffer,"33");
		addLine(buffer,"This is data that was returned by an origin server.");
		addLine(buffer,"0; ieof");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final IcapMessage createREQMODWithEarlyTerminatedPreviewAnnouncementIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod");
		request.addHeader("Host","icap-server.net");
		request.addHeader("Preview","151");
		request.setBody(EntryName.REQBODY);
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return request;
	}
	
	public static final IcapChunk createREQMODWithEarlyTerminatedPreviewIcapChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is data that was returned by an origin server.".getBytes("ASCII"));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		chunk.setIsPreviewChunk();
		return chunk;
	}
	
	public static final ChannelBuffer createREQMODWithEarlyTerminatedPreviewChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,"This is data that was returned by an origin server.");
		return buffer;
	}
	
	public static final IcapChunk createREQMODWithEarlyTerminatedPreviewLastIcapChunk() throws UnsupportedEncodingException {
		IcapChunk chunk = new DefaultIcapChunk(ChannelBuffers.EMPTY_BUFFER);
		chunk.setIsPreviewChunk();
		chunk.setIsEarlyTerminated();
		return chunk;
	}
	
	public static final ChannelBuffer createREQMODWithEarlyTerminatedPreviewLastChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"0; ieof");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final ChannelBuffer createREQMODWithEarlyTerminatedPreviewAnnouncement() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Preview: 151");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=169");
		addLine(buffer,null);
		addLine(buffer,"POST / HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain");
		addLine(buffer,"Accept-Encoding: compress");
		addLine(buffer,"Cookie: ff39fk3jur@4ii0e02i");
		addLine(buffer,"If-None-Match: \"xyzzy\", \"r2d2xxxx\"");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final void assertCreateREQMODWithEarlyTerminatedPreview(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=169",message);
		assertHeaderValue("Preview","151",message);
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
		assertFalse("preview chunk does not indicated that is is early terminated",chunk.isEarlyTerminated());
		assertChunk("preview chunk", chunk,"This is data that was returned by an origin server.",false);
	}
	
	public static final void assertCreateREQMODWithEarlyTerminatedPreviewLastChunk(IcapChunk chunk) {
		assertNotNull("preview last chunk was null",chunk);
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertTrue("preview chunk is not last chunk",chunk.isLast());
		assertTrue("preview chunk is not early terminated",chunk.isEarlyTerminated());
	}

	public static final ChannelBuffer createRESPMODWithGetRequestAndPreview() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, res-hdr=137, res-body=296");
		addLine(buffer,"Preview: 51");
		addLine(buffer,null);
		addLine(buffer,"GET /origin-resource HTTP/1.1");
		addLine(buffer,"Host: www.origin-server.com");
		addLine(buffer,"Accept: text/html, text/plain, image/gif");
		addLine(buffer,"Accept-Encoding: gzip, compress");
		addLine(buffer,null);
		addLine(buffer,"HTTP/1.1 200 OK");
		addLine(buffer,"Date: Mon, 10 Jan 2000 09:52:22 GMT");
		addLine(buffer,"Server: Apache/1.3.6 (Unix)");
		addLine(buffer,"ETag: \"63840-1ab7-378d415b\"");
		addLine(buffer,"Content-Type: text/html");
		addLine(buffer,"Content-Length: 151");
		addLine(buffer,null);
		addChunk(buffer,"This is data that was returned by an origin server.");
		addChunk(buffer,null);
		return buffer;
	}	
	
	public static final void assertCreateRESPMODWithGetRequestAndPreview(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, res-hdr=137, res-body=296",message);
		assertHeaderValue("Preview","51",message);
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
		assertHttpMessageHeaderValue("Content-Length","151",message.getHttpResponse());
	}

	public static final void assertCreateRESPMODWithGetRequestAndPreviewChunk(IcapChunk chunk) {
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertFalse("preview chunk indicated that is is early terminated",chunk.isEarlyTerminated());
		assertChunk("preview chunk", chunk,"This is data that was returned by an origin server.",false);
	}
	
	public static final void assertCreateRESPMODWithGetRequestAndPreviewLastChunk(IcapChunk chunk) {
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertTrue("preview chunk is not last chunk",chunk.isLast());
		assertFalse("preview chunk states that it is early terminated",chunk.isEarlyTerminated());
	}
	
	private static final void addLine(ChannelBuffer buffer, String value) throws UnsupportedEncodingException {
		if(value == null) {
			buffer.writeBytes(IcapCodecUtil.CRLF);
		} else {
			buffer.writeBytes(value.getBytes("ASCII"));
			buffer.writeBytes(IcapCodecUtil.CRLF);
		}
	}
	
	private static void addChunk(ChannelBuffer buffer, String chunkData) throws UnsupportedEncodingException {
		if(chunkData == null) {
			addLine(buffer,"0");
			addLine(buffer,null);
			addLine(buffer,null);
		} else {
			int length = chunkData.length();
			String hex = Integer.toString(length,16);
			addLine(buffer,hex);
			addLine(buffer,chunkData);
		}
	}
	
	private static void assertHeaderValue(String key, String expected, IcapMessage message) {
		assertNotNull("Message was null",message);
		assertTrue("Key does not exist [" + key + "]",message.containsHeader(key));
		assertEquals("The header: " + key + " is invalid",expected,message.getHeader(key));
	}
	
	private static void assertTrailingHeaderValue(String key, String expected, IcapChunkTrailer message) {
		assertNotNull("Chunk trailer was null",message);
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
