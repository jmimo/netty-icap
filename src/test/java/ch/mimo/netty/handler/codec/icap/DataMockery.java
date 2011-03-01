package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class DataMockery {
	
	private static final String LINEBREAK = "\r\n";

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
	
	public static final ChannelBuffer createOPTIONSRequest() {
		StringBuilder builder = new StringBuilder();
		addLine(builder,"OPTIONS icap://icap.mimo.ch:1344/reqmod ICAP/1.0");
		addLine(builder,"Host: icap.google.com:1344");
		addLine(builder,null);
		return ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
	}
	
	private static final void addLine(StringBuilder builder, String value) {
		if(value == null) {
			builder.append(LINEBREAK);
		} else {
			builder.append(value).append(LINEBREAK);
		}
	}
}
