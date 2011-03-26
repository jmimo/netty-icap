package ch.mimo.netty.handler.codec.icap;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.junit.Test;

public class SimpleNioNioSocketTest extends AbstractSocketTest {

	@Override
	protected ChannelFactory newServerSocketChannelFactory(Executor executor) {
		return new NioServerSocketChannelFactory(executor, executor);
	}

	@Override
	protected ChannelFactory newClientSocketChannelFactory(Executor executor) {
		 return new NioClientSocketChannelFactory(executor, executor);
	}

	@Test
	public void runTest() {
		
		try {
			super.runDecoderTest(new Handler(),new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.OPTIONS,"/bla/bla"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class Handler extends TestMessageHandler {

		@Override
		public void doMessageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception {
			// TODO Auto-generated method stub
		}
		
	}
}
