package ch.mimo.netty.handler.codec.icap.socket;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

public abstract class AbstractClientHandler extends SimpleChannelDownstreamHandler {

	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		doMessageReceived(ctx,e);
	}
	
	public abstract void doMessageReceived(ChannelHandlerContext context, ChannelEvent event) throws Exception;
}
