package ch.mimo.netty.handler.codec.icap.socket;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;

public class OioNioSocketTest extends SocketTests {

	@Override
	protected ChannelFactory newClientSocketChannelFactory(Executor executor) {
		 return new OioClientSocketChannelFactory(executor);
	}
	
	@Override
	protected ChannelFactory newServerSocketChannelFactory(Executor executor) {
		return new NioServerSocketChannelFactory(executor, executor);
	}
}
