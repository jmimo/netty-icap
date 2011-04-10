package ch.mimo.netty.handler.codec.icap.socket;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;

public class NioOioSocketTest extends SocketTests {

	@Override
	protected ChannelFactory newClientSocketChannelFactory(Executor executor) {
		 return new NioClientSocketChannelFactory(executor, executor);
	}
	
	@Override
	protected ChannelFactory newServerSocketChannelFactory(Executor executor) {
		return new OioServerSocketChannelFactory(executor, executor);
	}
}
