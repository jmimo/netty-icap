package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public abstract class TestMessageHandler extends SimpleChannelUpstreamHandler {

	// TODO add constructor that takes a Assertion interface which can be
	// implemented against in order to assert a certain message.
	// TODO behavior like the possibility to send a 100 continue
	
	@Override
	public final void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception {
		doMessageReceived(context,event);
	}

	public abstract void doMessageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception;
}
