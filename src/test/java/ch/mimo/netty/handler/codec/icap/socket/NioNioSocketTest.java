package ch.mimo.netty.handler.codec.icap.socket;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class NioNioSocketTest extends SocketTests {

	@Override
	protected ChannelFactory newServerSocketChannelFactory(Executor executor) {
		return new NioServerSocketChannelFactory(executor, executor);
	}

	@Override
	protected ChannelFactory newClientSocketChannelFactory(Executor executor) {
		 return new NioClientSocketChannelFactory(executor, executor);
	}
}
