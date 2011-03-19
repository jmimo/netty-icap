package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.internal.StringUtil;

public final class DataMockery {

	private DataMockery() {

	}
	
	public static final ChannelBuffer createWhiteSpacePrefixedOPTIONSRequest() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"  OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap.google.com:1344");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
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
	
	public static final ChannelBuffer createRESPMODWithGetRequestAndBody() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"RESPMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap-server.net");
		addLine(builder,"Encapsulated: req-hdr=0, res-hdr=137, res-body=296");
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
		addLine(builder,"33");
		addLine(builder,"This is data that was returned by an origin server.");
		addLine(builder,"0");
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	public static final ChannelBuffer createOPTIONSRequest() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap.google.com:1344");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	private static final void addLine(StringBuilder builder, String value) {
		if(value == null) {
			builder.append(StringUtil.NEWLINE);
		} else {
			builder.append(value).append(StringUtil.NEWLINE);
		}
	}
}
