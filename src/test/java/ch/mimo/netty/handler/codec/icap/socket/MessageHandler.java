package ch.mimo.netty.handler.codec.icap.socket;

import org.jboss.netty.channel.ChannelUpstreamHandler;

public interface MessageHandler extends ChannelUpstreamHandler {

	void close();
}
