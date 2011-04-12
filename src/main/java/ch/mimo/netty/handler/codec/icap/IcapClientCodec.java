package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;

public class IcapClientCodec implements ChannelUpstreamHandler, ChannelDownstreamHandler {

	private IcapRequestEncoder encoder = new IcapRequestEncoder();
	private IcapResponseDecoder decoder;
	
	public IcapClientCodec(int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
		decoder = new IcapResponseDecoder(maxInitialLineLength,maxIcapHeaderSize,maxHttpHeaderSize,maxChunkSize);
	}
	
	public IcapClientCodec() {
		this(4096,8192,8192,8192);
	}
	
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		encoder.handleDownstream(ctx,e);
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		decoder.handleUpstream(ctx,e);
	}

}
