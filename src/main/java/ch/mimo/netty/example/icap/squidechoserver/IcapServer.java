package ch.mimo.netty.example.icap.squidechoserver;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class IcapServer {

	private static final int SERVER_PORT = 1344;
	
	public static void main(String[] args) {
        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new IcapServerChannelPipeline());

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(SERVER_PORT));
	}
}
