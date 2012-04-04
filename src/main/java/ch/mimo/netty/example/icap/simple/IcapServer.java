/*******************************************************************************
 * Copyright 2011 - 2012 Michael Mimo Moratti
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ch.mimo.netty.example.icap.simple;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;

/**
 * An ICAP Server that prints the request and sends back the content of the request that was receveid.
 * OPTIONS is not supported.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class IcapServer {

	public static void main(String[] args) {
        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new OioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new IcapServerChannelPipeline());

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(8099));
	}
}
