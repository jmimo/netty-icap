/*******************************************************************************
 * Copyright (c) 2011 Michael Mimo Moratti.
 *  
 * Michael Mimo Moratti licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package ch.mimo.netty.handler.codec.icap.socket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.util.internal.ExecutorUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import ch.mimo.netty.handler.codec.icap.IcapClientCodec;
import ch.mimo.netty.handler.codec.icap.IcapRequestDecoder;
import ch.mimo.netty.handler.codec.icap.IcapRequestEncoder;
import ch.mimo.netty.handler.codec.icap.IcapResponseDecoder;
import ch.mimo.netty.handler.codec.icap.IcapResponseEncoder;
import ch.mimo.netty.handler.codec.icap.IcapServerCodec;


public abstract class AbstractSocketTest extends Assert {
	
	private static ExecutorService executor;
	
    private static final InetAddress LOCALHOST;

    static {
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            try {
                localhost = InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 });
            } catch (UnknownHostException e1) {
                try {
                    localhost = InetAddress.getByAddress(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 });
                } catch (UnknownHostException e2) {
                    System.err.println("Failed to get the localhost.");
                    e2.printStackTrace();
                }
            }
        }

        LOCALHOST = localhost;
    }
	
	@BeforeClass
	public static void init() {
		executor = Executors.newCachedThreadPool();
	}
	
	@AfterClass
	public static void destroy() {
		ExecutorUtil.terminate(executor);
	}
	
    protected abstract ChannelFactory newServerSocketChannelFactory(Executor executor);
    protected abstract ChannelFactory newClientSocketChannelFactory(Executor executor);
    
    protected void runSocketTest(Handler serverHandler, Handler clientHandler, Object[] messages, boolean useCodec) {
        ServerBootstrap serverBootstrap  = new ServerBootstrap(newServerSocketChannelFactory(executor));
        ClientBootstrap clientBootstrap = new ClientBootstrap(newClientSocketChannelFactory(executor));
        
        if(useCodec) {
        	setupCodecPipline(serverBootstrap,clientBootstrap,serverHandler,clientHandler);
        } else {
        	setupClassicPipline(serverBootstrap,clientBootstrap,serverHandler,clientHandler);
        }
        
        Channel serverChannel = serverBootstrap.bind(new InetSocketAddress(0));
        int port = ((InetSocketAddress)serverChannel.getLocalAddress()).getPort();
        
        ChannelFuture channelFuture = clientBootstrap.connect(new InetSocketAddress(LOCALHOST,port));
        assertTrue(channelFuture.awaitUninterruptibly().isSuccess());

        Channel clientChannel = channelFuture.getChannel();
        
        for(Object message : messages) {
        	ChannelFuture requestFuture = clientChannel.write(message);
        	assertTrue(requestFuture.awaitUninterruptibly().isSuccess());
        }
        
        while(!clientHandler.isProcessed()) {
        	if(clientHandler.hasException()) {
        		break;
        	}
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // NOOP
            }        	
        }
        
        while(!serverHandler.isProcessed()) {
        	if(serverHandler.hasException()) {
        		break;
        	}
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // NOOP
            }  
        }
        
        serverHandler.close();
        clientHandler.close();
        serverChannel.close().awaitUninterruptibly();
        
        if(serverHandler.hasException()) {
        	serverHandler.getExceptionCause().printStackTrace();
        	fail("Server Handler has experienced an exception");
        }
        
        if(clientHandler.hasException()) {
        	clientHandler.getExceptionCause().printStackTrace();
        	fail("Server Handler has experienced an exception");
        }
    }
    
    protected void setupClassicPipline(ServerBootstrap serverBootstrap, ClientBootstrap clientBootstrap, Handler serverHandler, Handler clientHandler) {
        // TODO think about logging handlers here
        
//      serverBootstrap.getPipeline().addLast("logging",new LoggingHandler());
    	serverBootstrap.getPipeline().addLast("decoder",new IcapRequestDecoder());
    	serverBootstrap.getPipeline().addLast("encoder",new IcapResponseEncoder());
    	serverBootstrap.getPipeline().addLast("handler",(SimpleChannelUpstreamHandler)serverHandler);
    	
      
//    	clientBootstrap.getPipeline().addLast("logging",new LoggingHandler());
    	clientBootstrap.getPipeline().addLast("encoder",new IcapRequestEncoder());
      	// TODO handler that let's the data trickle to the other side so that the receiver will be forced to re-read.
      	clientBootstrap.getPipeline().addLast("decoder",new IcapResponseDecoder());
      	clientBootstrap.getPipeline().addLast("handler",(SimpleChannelUpstreamHandler)clientHandler);
    }
    
    protected void setupCodecPipline(ServerBootstrap serverBootstrap, ClientBootstrap clientBootstrap, Handler serverHandler, Handler clientHandler) {
    	serverBootstrap.getPipeline().addLast("codec",new IcapServerCodec());
    	serverBootstrap.getPipeline().addLast("handler",(SimpleChannelUpstreamHandler)serverHandler);
    	
    	clientBootstrap.getPipeline().addLast("codec",new IcapClientCodec());
    	clientBootstrap.getPipeline().addLast("handler",(SimpleChannelUpstreamHandler)clientHandler);
    }
}
