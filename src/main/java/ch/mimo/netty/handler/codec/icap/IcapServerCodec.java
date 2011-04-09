package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;

public class IcapServerCodec implements ChannelUpstreamHandler, ChannelDownstreamHandler {

	private final IcapRequestDecoder decoder;
	private final IcapResponseEncoder encoder = new IcapResponseEncoder();
	
	public IcapServerCodec() {
		this(4096,8192,8192,8192);
	}
	
	public IcapServerCodec(int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
		decoder = new IcapRequestDecoder(maxInitialLineLength,maxIcapHeaderSize,maxHttpHeaderSize,maxChunkSize);
	}
	
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		encoder.handleDownstream(ctx, e);
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		decoder.handleUpstream(ctx, e);
	}

}
