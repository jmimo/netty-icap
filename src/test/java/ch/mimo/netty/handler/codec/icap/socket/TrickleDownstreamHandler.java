package ch.mimo.netty.handler.codec.icap.socket;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;

public class TrickleDownstreamHandler implements ChannelDownstreamHandler {

	private long latency;
	
	public TrickleDownstreamHandler(long latency) {
		this.latency = latency;
	}
	
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		// TODO unwrap channel and trickle bytes...
		Thread.sleep(latency);
		ctx.sendDownstream(e);
	}

}
