package ch.mimo.netty.handler.codec.icap.socket;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;

public class OioOioSocketTest extends SocketTests {

	@Override
	protected ChannelFactory newClientSocketChannelFactory(Executor executor) {
		 return new OioClientSocketChannelFactory(executor);
	}
	
	@Override
	protected ChannelFactory newServerSocketChannelFactory(Executor executor) {
		return new OioServerSocketChannelFactory(executor, executor);
	}
}
