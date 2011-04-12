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

import java.io.UnsupportedEncodingException;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.junit.Test;

import ch.mimo.netty.handler.codec.icap.DataMockery;
import ch.mimo.netty.handler.codec.icap.IcapChunk;
import ch.mimo.netty.handler.codec.icap.IcapRequest;
import ch.mimo.netty.handler.codec.icap.IcapResponse;

public abstract class SocketTests extends AbstractSocketTest {
	
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
		
		runSocketTest(serverHandler,clientHandler,new Object[]{DataMockery.createOPTIONSIcapRequest()},false);
		runSocketTest(serverHandler,clientHandler,new Object[]{DataMockery.createOPTIONSIcapRequest()},true);
	}
	
	@Test
	public void sendRESPMODWithGetRequestNoBody() {
		Handler serverHandler = new AbstractHandler() {
			
			@Override
			public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
				IcapRequest request = (IcapRequest)event.getMessage();
				DataMockery.assertCreateRESPMODWithGetRequestNoBody(request);
				channel.write(DataMockery.createRESPMODWithGetRequestNoBodyIcapResponse());
				return true;
			}
		};
		
		Handler clientHandler = new AbstractHandler() {
			
			@Override
			public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
				IcapResponse response = (IcapResponse)event.getMessage();
				DataMockery.assertRESPMODWithGetRequestNoBodyResponse(response);
				return true;
			}
		};
		
		runSocketTest(serverHandler,clientHandler,new Object[]{DataMockery.createRESPMODWithGetRequestNoBodyIcapMessage()},false);
		runSocketTest(serverHandler,clientHandler,new Object[]{DataMockery.createRESPMODWithGetRequestNoBodyIcapMessage()},true);
	}
	
	@Test
	public void sendREQMODWithTwoBodyChunk() throws UnsupportedEncodingException {
		Handler serverHandler = new AbstractHandler() {
			boolean requestMessage = false;
			boolean firstChunk = false;
			boolean secondChunk = false;
			boolean thirdChunk = false;
			@Override
			public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
				Object msg = event.getMessage();
				if(msg instanceof IcapRequest) {
					IcapRequest request = (IcapRequest)event.getMessage();
					DataMockery.assertCreateREQMODWithTwoChunkBody(request);
					requestMessage = true;
				} else if(msg instanceof IcapChunk) {
					IcapChunk chunk = (IcapChunk)msg;
					if(!firstChunk) {
						DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk(chunk);
						firstChunk = true;
					} else if(firstChunk & !secondChunk) {
						DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk(chunk);
						secondChunk = true;
					} else if(firstChunk & secondChunk & !thirdChunk) {
						DataMockery.assertCreateREQMODWithTwoChunkBodyThirdChunk(chunk);
						channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapResponse());
						channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
						channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
						channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree());
						thirdChunk = true;
					}
				} else {
					fail("unexpected msg instance [" + msg.getClass().getCanonicalName() + "]");
				}
				return requestMessage & firstChunk & secondChunk & thirdChunk;
			}
		};
		
		Handler clientHandler = new AbstractHandler() {
			boolean responseMessage = false;
			boolean firstChunk = false;
			boolean secondChunk = false;
			boolean thirdChunk = false;
			@Override
			public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
				Object msg = event.getMessage();
				if(msg instanceof IcapResponse) {
					IcapResponse response = (IcapResponse)event.getMessage();
					DataMockery.assertREQMODWithTwoChunkBodyResponse(response);
					responseMessage = true;
				} else if(msg instanceof IcapChunk) {
					IcapChunk chunk = (IcapChunk)msg;
					if(!firstChunk) {
						DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk(chunk);
						firstChunk = true;
					} else if(firstChunk & !secondChunk) {
						DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk(chunk);
						secondChunk = true;
					} else if(firstChunk & secondChunk & !thirdChunk) {
						DataMockery.assertCreateREQMODWithTwoChunkBodyThirdChunk(chunk);
						thirdChunk = true;
					}
				} else {
					fail("unexpected msg instance [" + msg.getClass().getCanonicalName() + "]");
				}
				return responseMessage & firstChunk & secondChunk & thirdChunk;
			}
		};
		
		runSocketTest(serverHandler,clientHandler,new Object[]{DataMockery.createREQMODWithTwoChunkBodyIcapMessage(),
				DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne(),DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo(),
				DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree()},false);
		runSocketTest(serverHandler,clientHandler,new Object[]{DataMockery.createREQMODWithTwoChunkBodyIcapMessage(),
				DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne(),DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo(),
				DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree()},true);
	}
}

