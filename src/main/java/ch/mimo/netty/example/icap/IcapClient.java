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
package ch.mimo.netty.example.icap;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import ch.mimo.netty.handler.codec.icap.DefaultIcapRequest;
import ch.mimo.netty.handler.codec.icap.IcapMethod;
import ch.mimo.netty.handler.codec.icap.IcapRequest;
import ch.mimo.netty.handler.codec.icap.IcapVersion;

/**
 * Simple ICAP client that send a REQMOD request with a HTTP POST request and body
 * to a server and prints the answer.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class IcapClient {

	public static void main(String[] args) {
			int port = 8099;
			String host = "localhost";
			
	        // Configure the client.
	        ClientBootstrap bootstrap = new ClientBootstrap(
	                new NioClientSocketChannelFactory(
	                        Executors.newCachedThreadPool(),
	                        Executors.newCachedThreadPool()));

	        // Set up the event pipeline factory.
	        bootstrap.setPipelineFactory(new IcapClientChannlePipeline());

	        // Start the connection attempt.
	        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,port));

	        // Wait until the connection attempt succeeds or fails.
	        Channel channel = future.awaitUninterruptibly().getChannel();
	        if (!future.isSuccess()) {
	            future.getCause().printStackTrace();
	            bootstrap.releaseExternalResources();
	            return;
	        }

	        // Prepare the ICAP request.
	        IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"/simple","localhost");     
	        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,"/some/servers/uri");
	        httpRequest.setHeader(HttpHeaders.Names.HOST,host);
	        httpRequest.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
	        httpRequest.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
	        httpRequest.setContent(ChannelBuffers.wrappedBuffer("This is the message body that contains all the necessary data to answer the ultimate question...".getBytes()));
	        request.setHttpRequest(httpRequest);
	        
	        // Send the ICAP request.
	        channel.write(request);

	        // Wait for the server to close the connection.
	        channel.getCloseFuture().awaitUninterruptibly();

	        // Shut down executor threads to exit.
	        bootstrap.releaseExternalResources();
	}
}
