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
	
	public static final IcapRequest createOPTIONSIcapRequest() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.OPTIONS,"icap://icap.mimo.ch:1344/reqmod","icap.google.com:1344");
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
	
	public static final IcapResponse createOPTIONSIcapResponse() {
		IcapResponse response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
		response.addHeader("Methods","REQMOD RESPMOD");
		response.addHeader("Service","Joggels icap server 1.0");
		response.addHeader("ISTag","5BDEEEA9-12E4-2");
		response.addHeader("Max-Connections","100");
		response.addHeader("Options-TTL","1000");
		response.addHeader("Allow","204");
		response.addHeader("Preview","1024");
		return response;
	}
	
	public static final ChannelBuffer createOPTIONSResponse() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 200 OK");
		addLine(buffer,"Methods: REQMOD RESPMOD");
		addLine(buffer,"Service: Joggels icap server 1.0");
		addLine(buffer,"ISTag: 5BDEEEA9-12E4-2");
		addLine(buffer,"Max-Connections: 100");
		addLine(buffer,"Options-TTL: 1000");
		addLine(buffer,"Allow: 204");
		addLine(buffer,"Preview: 1024");
		addLine(buffer,"Encapsulated: null-body=0");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final void assertOPTIONSResponse(IcapResponse response) {
		assertEquals("wrong protocol version",IcapVersion.ICAP_1_0,response.getProtocolVersion());
		assertEquals("response code not as expected",IcapResponseStatus.OK,response.getStatus());
		assertHeaderValue("Methods","REQMOD RESPMOD",response);
		assertHeaderValue("Service","Joggels icap server 1.0",response);
		assertHeaderValue("ISTag","5BDEEEA9-12E4-2",response);
		assertHeaderValue("Max-Connections","100",response);
		assertHeaderValue("Options-TTL","1000",response);
		assertHeaderValue("Allow","204",response);
		assertHeaderValue("Preview","1024",response);
		assertHeaderValue("Encapsulated","null-body=0",response);
	}
	
	public static final IcapResponse createOPTIONSIcapResponseWithBody() {
		IcapResponse response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
		response.addHeader("Methods","REQMOD RESPMOD");
		response.addHeader("Service","Joggels icap server 1.0");
		response.addHeader("ISTag","5BDEEEA9-12E4-2");
		response.addHeader("Max-Connections","100");
		response.addHeader("Options-TTL","1000");
		response.addHeader("Allow","204");
		response.addHeader("Preview","1024");
		response.addHeader("Opt-body-type","Simple-text");
		response.setBody(IcapMessageElementEnum.OPTBODY);
		return response;
	}
	
	public static final ChannelBuffer createOPTIONSResponseWithBody() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 200 OK");
		addLine(buffer,"Methods: REQMOD RESPMOD");
		addLine(buffer,"Service: Joggels icap server 1.0");
		addLine(buffer,"ISTag: 5BDEEEA9-12E4-2");
		addLine(buffer,"Max-Connections: 100");
		addLine(buffer,"Options-TTL: 1000");
		addLine(buffer,"Allow: 204");
		addLine(buffer,"Preview: 1024");
		addLine(buffer,"Opt-body-type: Simple-text");
		addLine(buffer,"Encapsulated: opt-body=0");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final IcapChunk createOPTIONSIcapChunk() {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is a sample Options response body text".getBytes(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		return chunk;
	}
	
	public static final ChannelBuffer createOPTIONSChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"2b");
		addLine(buffer,"This is a sample Options response body text");
		return buffer;
	}
	
	public static final IcapChunk createOPTIONSLastIcapChunk() {
		IcapChunk chunk = new DefaultIcapChunk(ChannelBuffers.EMPTY_BUFFER);
		return chunk;
	}
	
	public static final ChannelBuffer createOPTIONSLastChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"0");
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
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.OPTIONS,"icap://icap.mimo.ch:1344/reqmod","icap.google.com:1344");
		request.setBody(IcapMessageElementEnum.OPTBODY);
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
		return new DefaultIcapChunkTrailer();
	}
	
	public static final void assertOPTIONSRequestWithBodyLastChunk(IcapChunk chunk) {
		assertChunk("options last chunk",chunk,null,true);
	}
	
	public static final ChannelBuffer createREQMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, null-body=170");
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
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return request;
	}
	
	public static final IcapRequest createREQMODWithGetRequestNoBodyAndEncapsulationHeaderIcapMessage() {
		IcapRequest request = createREQMODWithGetRequestNoBodyIcapMessage();
		request.addHeader("Encapsulated","req-hdr=0, null-body=170");
		return request;
	}
	
	public static final IcapRequest createREQMODWithGetRequestNoBodyAndEncapsulationHeaderAndNullBodySetIcapMessage() {
		IcapRequest request = createREQMODWithGetRequestNoBodyIcapMessage();
		request.addHeader("Encapsulated","req-hdr=0, null-body=170");
		request.setBody(IcapMessageElementEnum.NULLBODY);
		return request;
	}
	
	public static final void assertCreateREQMODWithGetRequestNoBody(IcapMessage message) {
		assertNotNull("the request was null",message);
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
	
	public static final IcapResponse createREQMODWithGetRequestNoBodyIcapResponse() {
		IcapResponse response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
		response.addHeader("Host","icap-server.net");
		response.addHeader("ISTag","Serial-0815");
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/");
		response.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return response;
	}
	
	public static final ChannelBuffer createREQMODWithGetRequestResponse() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 200 OK");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"ISTag: Serial-0815");
		addLine(buffer,"Encapsulated: req-hdr=0, null-body=170");
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
	
	public static final void assertREQMODWithGetRequestResponse(IcapResponse response) {
		assertEquals("wrong protocol version",IcapVersion.ICAP_1_0,response.getProtocolVersion());
		assertEquals("response code not as expected",IcapResponseStatus.OK,response.getStatus());
		assertHeaderValue("Host","icap-server.net",response);
		assertHeaderValue("ISTag","Serial-0815",response);
		assertHeaderValue("Encapsulated","req-hdr=0, null-body=170",response);
		HttpRequest httpRequest = response.getHttpRequest();
		assertNotNull("http request was null",httpRequest);
		assertEquals("http request was of wrong type",HttpMethod.GET,httpRequest.getMethod());
		assertEquals("http request was of wrong version",HttpVersion.HTTP_1_1,httpRequest.getProtocolVersion());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",httpRequest);
		assertHttpMessageHeaderValue("Accept","text/html, text/plain",httpRequest);
		assertHttpMessageHeaderValue("Accept-Encoding","compress",httpRequest);
		assertHttpMessageHeaderValue("Cookie","ff39fk3jur@4ii0e02i",httpRequest);
		assertHttpMessageHeaderValue("If-None-Match","\"xyzzy\", \"r2d2xxxx\"",httpRequest);

	}
	
	public static final ChannelBuffer createRESPMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, res-hdr=137, null-body=296");
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
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.RESPMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
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
	
	public static final IcapResponse createRESPMODWithGetRequestNoBodyIcapResponse() {
		IcapResponse response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
		response.addHeader("Host","icap-server.net");
		response.addHeader("ISTag","Serial-0815");
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/origin-resource");
		response.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain, image/gif");
		httpRequest.addHeader("Accept-Encoding","gzip, compress");
		HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
		response.setHttpResponse(httpResponse);
		httpResponse.addHeader("Date","Mon, 10 Jan 2000 09:52:22 GMT");
		httpResponse.addHeader("Server","Apache/1.3.6 (Unix)");
		httpResponse.addHeader("ETag","\"63840-1ab7-378d415b\"");
		httpResponse.addHeader("Content-Type","text/html");
		httpResponse.addHeader("Content-Length","51");
		return response;
	}
	
	public static final ChannelBuffer createRESPMODWithGetRequestNoBodyResponse() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 200 OK");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"ISTag: Serial-0815");
		addLine(buffer,"Encapsulated: req-hdr=0, res-hdr=137, null-body=296");
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
	
	public static final void assertRESPMODWithGetRequestNoBodyResponse(IcapResponse response) {
		assertEquals("wrong protocol version",IcapVersion.ICAP_1_0,response.getProtocolVersion());
		assertEquals("response code not as expected",IcapResponseStatus.OK,response.getStatus());
		assertHeaderValue("Host","icap-server.net",response);
		assertHeaderValue("ISTag","Serial-0815",response);
		assertHeaderValue("Encapsulated","req-hdr=0, res-hdr=137, null-body=296",response);
		HttpRequest httpRequest = response.getHttpRequest();
		assertNotNull("http request was null",httpRequest);
		assertEquals("http request was of wrong type",HttpMethod.GET,httpRequest.getMethod());
		assertEquals("http request was of wrong version",HttpVersion.HTTP_1_1,httpRequest.getProtocolVersion());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",httpRequest);
		assertHttpMessageHeaderValue("Accept","text/html, text/plain, image/gif",httpRequest);
		assertHttpMessageHeaderValue("Accept-Encoding","gzip, compress",httpRequest);
		HttpResponse httpResponse = response.getHttpResponse();
		assertNotNull("http response was null",httpResponse);
		assertEquals("http response was of wrong version",HttpVersion.HTTP_1_1,httpResponse.getProtocolVersion());
		assertEquals("http response status was wrong",HttpResponseStatus.OK,httpResponse.getStatus());
		assertHttpMessageHeaderValue("Date","Mon, 10 Jan 2000 09:52:22 GMT",httpResponse);
		assertHttpMessageHeaderValue("Server","Apache/1.3.6 (Unix)",httpResponse);
		assertHttpMessageHeaderValue("ETag","\"63840-1ab7-378d415b\"",httpResponse);
		assertHttpMessageHeaderValue("Content-Type","text/html",httpResponse);
		assertHttpMessageHeaderValue("Content-Length","51",httpResponse);
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
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=171");
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
	
	public static final ChannelBuffer createREQMODWithImplicitTwoChunkBodyResponse() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 200 OK");
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
	
	public static final void assertCreateREQMODWithImplicitTwoChunkBodyResponse(IcapResponse message) {
		assertEquals("response was of wrong version",IcapVersion.ICAP_1_0,message.getProtocolVersion());
		assertEquals("response had wrong code",IcapResponseStatus.OK,message.getStatus());
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
	
	public static final ChannelBuffer createREQMODWithTwoChunkBodyAnnouncement() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=171");
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
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
		request.setBody(IcapMessageElementEnum.REQBODY);
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return request;
	}
	
	public static final IcapMessage createREQMODWithTwoChunkBodyAndEncapsulationHeaderIcapMessage() {
		IcapMessage request = createREQMODWithTwoChunkBodyIcapMessage();
		request.addHeader("Encapsulated","req-hdr=0, req-body=171");
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
		return new DefaultIcapChunkTrailer();
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
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=171");
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
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=171",message);
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
		assertTrue("last chunk is wrong type",chunk instanceof IcapChunkTrailer);
		assertChunk("third chunk",chunk,null,true);
	}
	
	public static final void assertCreateREQMODWithTwoChunkBodyTrailingHeaderChunk(IcapChunkTrailer trailer) {
		assertNotNull("trailer is null",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey1","TrailingHeaderValue1",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey2","TrailingHeaderValue2",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey3","TrailingHeaderValue3",trailer);
		assertTrailingHeaderValue("TrailingHeaderKey4","TrailingHeaderValue4",trailer);
	}
	
	public static final IcapResponse createREQMODWithTwoChunkBodyIcapResponse() {
		IcapResponse response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
		response.addHeader("Host","icap-server.net");
		response.addHeader("ISTag","Serial-0815");
		response.setBody(IcapMessageElementEnum.REQBODY);
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
		response.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return response;
	}
	
	public static final void assertREQMODWithTwoChunkBodyResponse(IcapResponse response) {
		assertHeaderValue("Host","icap-server.net",response);
		assertHeaderValue("ISTag","Serial-0815",response);
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=171",response);
		assertNotNull("http request was null",response.getHttpRequest());
		assertEquals("http request method was wrong",HttpMethod.POST,response.getHttpRequest().getMethod());
		assertHttpMessageHeaderValue("Host","www.origin-server.com",response.getHttpRequest());
		assertHttpMessageHeaderValue("Accept","text/html, text/plain",response.getHttpRequest());
		assertHttpMessageHeaderValue("Accept-Encoding","compress",response.getHttpRequest());
		assertHttpMessageHeaderValue("Cookie","ff39fk3jur@4ii0e02i",response.getHttpRequest());
		assertHttpMessageHeaderValue("If-None-Match","\"xyzzy\", \"r2d2xxxx\"",response.getHttpRequest());
	}
	
	public static final ChannelBuffer createREQMODWithTwoChunkBodyResponse() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 200 OK");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"ISTag: Serial-0815");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=171");
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
	
	public static final ChannelBuffer createREQMODWithPreview() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(buffer,"Host: icap-server.net");
		addLine(buffer,"Preview: 51");
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=171");
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
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=171");
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
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
		request.addHeader("Preview","51");
		request.setBody(IcapMessageElementEnum.REQBODY);
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		return request;
	}
	
	public static final IcapMessage createREQMODWithPreviewAnnouncement204ResponseIcapMessage() {
		return new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.NO_CONTENT);
	}
	
	public static final IcapResponse createREQMODWithPreviewAnnouncement100ContinueIcapMessage() {
		return new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.CONTINUE);
	}
	
	public static final void assertCreateREQMODWithPreviewAnnouncement204Response(IcapResponse response) {
		assertNotNull("response was null",response);
		assertEquals("wrong icap version",IcapVersion.ICAP_1_0,response.getProtocolVersion());
		assertEquals("wrong response status",IcapResponseStatus.NO_CONTENT,response.getStatus());
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
	
	public static final IcapChunk createREQMODWithPreview100ContinueIcapChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is the second chunk that is received when 100 continue was sent.".getBytes("ASCII"));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		return chunk;
	}
	
	public static final IcapChunk createREQMODWithPreview100ContinueLastIcapChunk() {
		return new DefaultIcapChunkTrailer(false,false);
	}
	
	public static final IcapChunk createREQMODWithPreviewLastIcapChunk() throws UnsupportedEncodingException {
		return new DefaultIcapChunkTrailer(true,false);
	}
	
	public static final ChannelBuffer createREQMODWithPreviewLastChunk() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addChunk(buffer,null);
		return buffer;
	}
	
	
	
	public static final void assertCreateREQMODWithPreview(IcapMessage message) {
		assertEquals("Uri is wrong","icap://icap.mimo.ch:1344/reqmod",message.getUri());
		assertHeaderValue("Host","icap-server.net",message);
		assertHeaderValue("Encapsulated","req-hdr=0, req-body=171",message);
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
		assertTrue("last chunk is wrong type",chunk instanceof IcapChunkTrailer);
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertTrue("preview chunk is not last chunk",chunk.isLast());
		assertFalse("preview chunk states that it is early terminated",chunk.isEarlyTerminated());
	}
	
	public static final void assertCreateREQMODWithPreview100ContinueChunk(IcapChunk chunk) {
		assertChunk("preview chunk", chunk,"This is the second chunk that is received when 100 continue was sent.",false);
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
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
		request.addHeader("Preview","151");
		request.setBody(IcapMessageElementEnum.REQBODY);
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
		addLine(buffer,"Encapsulated: req-hdr=0, req-body=171");
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
		assertTrue("last chunk is wrong type",chunk instanceof IcapChunkTrailer);
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
	
	public static final IcapRequest createRESPMODWithGetRequestAndPreviewIncludingEncapsulationHeaderIcapRequest() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.RESPMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
		request.addHeader("Encapsulated","req-hdr=0, res-hdr=137, res-body=296");
		request.addHeader("Preview","51");
		request.setBody(IcapMessageElementEnum.RESBODY);
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
		httpResponse.addHeader("Content-Length","151");
		return request;
	}
	
	public static final IcapChunk createRESPMODWithGetRequestAndPreviewIcapChunk() {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is data that was returned by an origin server.".getBytes(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk = new DefaultIcapChunk(buffer);
		chunk.setIsPreviewChunk();
		return chunk;
	}
	
	public static final IcapChunk crateRESPMODWithGetRequestAndPreviewLastIcapChunk() {
		IcapChunkTrailer trailer = new DefaultIcapChunkTrailer();
		trailer.setIsPreviewChunk();
		return trailer;
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
	
	public static final ChannelBuffer createRESPMODWithGetRequestAndPreviewResponse() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 200 OK");
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
	
	public static final void assertCreateRESPMODWithGetRequestAndPreviewResponse(IcapResponse message) {
		assertEquals("wrong response version",IcapVersion.ICAP_1_0,message.getProtocolVersion());
		assertEquals("wrong resonse status",IcapResponseStatus.OK,message.getStatus());
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
		assertTrue("last chunk is wrong type",chunk instanceof IcapChunkTrailer);
		assertTrue("preview chunk is not marked as such",chunk.isPreviewChunk());
		assertTrue("preview chunk is not last chunk",chunk.isLast());
		assertFalse("preview chunk states that it is early terminated",chunk.isEarlyTerminated());
	}
	
	public static final IcapResponse create100ContinueIcapResponse() {
		IcapResponse response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.CONTINUE);
		return response;
	}
	
	public static final ChannelBuffer create100ContinueResponse() throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		addLine(buffer,"ICAP/1.0 100 Continue");
		addLine(buffer,"Encapsulated: null-body=0");
		addLine(buffer,null);
		return buffer;
	}
	
	public static final IcapRequest createREQMODWithGetRequestAndDataIcapMessage() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/");
		request.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is data that was returned by an origin server.".getBytes(IcapCodecUtil.ASCII_CHARSET));
		httpRequest.setContent(buffer);
		return request;
	}
	
	public static final IcapResponse createREQMODWithDataIcapResponse() {
		IcapResponse response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
		response.addHeader("Host","icap-server.net");
		response.addHeader("ISTag","Serial-0815");
		response.setBody(IcapMessageElementEnum.REQBODY);
		HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
		response.setHttpRequest(httpRequest);
		httpRequest.addHeader("Host","www.origin-server.com");
		httpRequest.addHeader("Accept","text/html, text/plain");
		httpRequest.addHeader("Accept-Encoding","compress");
		httpRequest.addHeader("Cookie","ff39fk3jur@4ii0e02i");
		httpRequest.addHeader("If-None-Match","\"xyzzy\", \"r2d2xxxx\"");
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is data that was returned by an origin server.".getBytes(IcapCodecUtil.ASCII_CHARSET));
		httpRequest.setContent(buffer);
		return response;
	}
	
	public static final IcapRequest createRESPMODWithPreviewDataIcapRequest() {
		IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.RESPMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
		request.addHeader("Preview","51");
		request.setBody(IcapMessageElementEnum.RESBODY);
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
		httpResponse.addHeader("Content-Length","151");
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes("This is data that was returned by an origin server.".getBytes(IcapCodecUtil.ASCII_CHARSET));
		httpResponse.setContent(buffer);
		return request;
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
