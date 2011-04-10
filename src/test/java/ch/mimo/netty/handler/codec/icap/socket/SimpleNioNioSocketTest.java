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

import java.util.concurrent.Executor;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.junit.Test;

import ch.mimo.netty.handler.codec.icap.DataMockery;
import ch.mimo.netty.handler.codec.icap.IcapRequest;
import ch.mimo.netty.handler.codec.icap.IcapResponse;

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
	public void sendOPTIONSRequest() {
		Handler serverHandler = new AbstractHandler() {
			@Override
			public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
				IcapRequest request = (IcapRequest)event.getMessage();
				DataMockery.assertCreateOPTIONSRequest(request);
				channel.write(DataMockery.createOPTIONSIcapResponse());
				return true;
			}
		};
		
		Handler clientHandler = new AbstractHandler() {
			@Override
			public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
				IcapResponse response = (IcapResponse)event.getMessage();
				DataMockery.assertOPTIONSResponse(response);
				return true;
			}
		};
		runDecoderTest(serverHandler,clientHandler,DataMockery.createOPTIONSIcapRequest());
	}
}

