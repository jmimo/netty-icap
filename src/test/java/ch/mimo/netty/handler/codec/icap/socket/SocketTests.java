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
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.junit.Test;

import ch.mimo.netty.handler.codec.icap.DataMockery;
import ch.mimo.netty.handler.codec.icap.IcapChunk;
import ch.mimo.netty.handler.codec.icap.IcapRequest;
import ch.mimo.netty.handler.codec.icap.IcapResponse;

public abstract class SocketTests extends AbstractSocketTest {
	
	private class SendOPTIONSRequestServerHandler extends AbstractHandler {
		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			IcapRequest request = (IcapRequest)event.getMessage();
			DataMockery.assertCreateOPTIONSRequest(request);
			channel.write(DataMockery.createOPTIONSIcapResponse());
			return true;
		}
	}
	
	private class SendOPTIONSRequestClientHandler extends AbstractHandler {
		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			IcapResponse response = (IcapResponse)event.getMessage();
			DataMockery.assertOPTIONSResponse(response);
			return true;
		}
	}
	
	private void sendOPTIONSRequest(PipelineType type) {
		runSocketTest(new SendOPTIONSRequestServerHandler(),new SendOPTIONSRequestClientHandler(),new Object[]{DataMockery.createOPTIONSIcapRequest()},type);
	}
	
	@Test
	public void sendOPTIONSRequestThroughClassicPipeline() {
		sendOPTIONSRequest(PipelineType.CLASSIC);
	}
	
	@Test
	public void sendOPTIONSRequestThroughCodecPipeline() {
		sendOPTIONSRequest(PipelineType.CODEC);
	}
	
	@Test
	public void sendOPTIONSRequestThroughTricklePipline() {
		sendOPTIONSRequest(PipelineType.TRICKLE);
	}
	
	private class SendRESPMODWithGetRequestNoBodyServerHandler extends AbstractHandler {
		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			IcapRequest request = (IcapRequest)event.getMessage();
			DataMockery.assertCreateRESPMODWithGetRequestNoBody(request);
			channel.write(DataMockery.createRESPMODWithGetRequestNoBodyIcapResponse());
			return true;
		}
	}
	
	private class SendRESPMODWithGetRequestNoBodyClientHandler extends AbstractHandler {
		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			IcapResponse response = (IcapResponse)event.getMessage();
			DataMockery.assertRESPMODWithGetRequestNoBodyResponse(response);
			return true;
		}
	}
	
	private void sendRESPMODWithGetRequestNoBody(PipelineType type) {
		runSocketTest(new SendRESPMODWithGetRequestNoBodyServerHandler(),new SendRESPMODWithGetRequestNoBodyClientHandler(),new Object[]{DataMockery.createRESPMODWithGetRequestNoBodyIcapMessage()},type);
	}
	
	@Test
	public void sendRESPMODWithGetRequestNoBodyThroughClassicPipeline() {
		sendRESPMODWithGetRequestNoBody(PipelineType.CLASSIC);
	}
	
	@Test
	public void sendRESPMODWithGetRequestNoBodyThroughCodecPipeline() {
		sendRESPMODWithGetRequestNoBody(PipelineType.CODEC);
	}
	
	@Test
	public void sendRESPMODWithGetRequestNoBodyThroughTricklePipeline() {
		sendRESPMODWithGetRequestNoBody(PipelineType.TRICKLE);
	}

	private class SendREQMODWithTwoBodyChunkServerHandler extends AbstractHandler {
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
	}
	
	private class SendREQMODWithTwoBodyChunkWithChunkAggregatorInPipelineServerHandler extends AbstractHandler {

		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			Object msg = event.getMessage();
			if(msg instanceof IcapRequest) {
				IcapRequest request = (IcapRequest)event.getMessage();
				DataMockery.assertCreateREQMODWithTwoChunkBody(request);
				ChannelBuffer contentBuffer = request.getHttpRequest().getContent();
				String body = contentBuffer.toString(Charset.forName("ASCII"));
				StringBuilder builder = new StringBuilder();
				builder.append("This is data that was returned by an origin server.");
				builder.append("And this the second chunk which contains more information.");
				assertEquals("The body content was wrong",builder.toString(),body);
				channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapResponse());
				channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
				channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
				channel.write(DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree());
				return true;
			}
			return false;
		}
	}
	
	private class SendREQMODWithTwoBodyChunkClientHandler extends AbstractHandler {
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
	}
	
	private void sendREQMODWithTwoBodyChunk(PipelineType type) {
		try {
			runSocketTest(new SendREQMODWithTwoBodyChunkServerHandler(),new SendREQMODWithTwoBodyChunkClientHandler(),new Object[]{DataMockery.createREQMODWithTwoChunkBodyIcapMessage(),
					DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne(),DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo(),
					DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree()},type);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("encoding error");
		}
	}
	
	private void sendREQMODWithTwoBodyChunkWithChunkAggregatorInPipeline() {
		try {
			runSocketTest(new SendREQMODWithTwoBodyChunkWithChunkAggregatorInPipelineServerHandler(),new SendREQMODWithTwoBodyChunkClientHandler(),new Object[]{DataMockery.createREQMODWithTwoChunkBodyIcapMessage(),
					DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne(),DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo(),
					DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree()},PipelineType.AGGREGATOR);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("encoding error");
		}
	}
	
	@Test
	public void sendREQMODWithTwoBodyChunkThroughClassicPipeline() {
		sendREQMODWithTwoBodyChunk(PipelineType.CLASSIC);
	}
	
	@Test
	public void sendREQMODWithTwoBodyChunkThroughCodecPipeline() {
		sendREQMODWithTwoBodyChunk(PipelineType.CODEC);
	}
	
	@Test
	public void sendREQMODWithTwoBodyChunkThroughTricklePipeline() {
		sendREQMODWithTwoBodyChunk(PipelineType.TRICKLE);
	}
	
	@Test
	public void sendREQMODWithTowBodyChunkThroughChunkAggregatorPipeline() {
		sendREQMODWithTwoBodyChunkWithChunkAggregatorInPipeline();
	}
	
	private class SendREQMODWithPreviewServerHandler extends AbstractHandler {
		boolean requestMessage = false;
		boolean firstChunk = false;
		boolean secondChunk = false;
		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			Object msg = event.getMessage();
			if(msg instanceof IcapRequest) {
				IcapRequest request = (IcapRequest)event.getMessage();
				DataMockery.assertCreateREQMODWithPreview(request);
				requestMessage = true;
			} else if(msg instanceof IcapChunk) {
				IcapChunk chunk = (IcapChunk)msg;
				if(!firstChunk) {
					DataMockery.assertCreateREQMODWithPreviewChunk(chunk);
					firstChunk = true;
				} else if(firstChunk & !secondChunk) {
					DataMockery.assertCreateREQMODWithPreviewChunkLastChunk(chunk);
					channel.write(DataMockery.createREQMODWithPreviewAnnouncement204ResponseIcapMessage());
					secondChunk = true;
				} 
			} else {
				fail("unexpected msg instance [" + msg.getClass().getCanonicalName() + "]");
			}
			return requestMessage & firstChunk & secondChunk;
		}
	}
	
	private class SendREQMODWithPreviewAggregatorServerHandler extends AbstractHandler {

		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			Object msg = event.getMessage();
			if(msg instanceof IcapRequest) {
				IcapRequest request = (IcapRequest)event.getMessage();
				DataMockery.assertCreateREQMODWithPreview(request);
				ChannelBuffer requestBodyBuffer = request.getHttpRequest().getContent();
				String body = requestBodyBuffer.toString(Charset.forName("ASCII"));
				StringBuilder builder = new StringBuilder();
				builder.append("This is data that was returned by an origin server.");
				assertEquals("The body content was wrong",builder.toString(),body);
				channel.write(DataMockery.createREQMODWithPreviewAnnouncement204ResponseIcapMessage());
				return true;
			} else {
				fail("unexpected msg instance [" + msg.getClass().getCanonicalName() + "]");
			}
			return false;
		}
		
	}
	
	private class SendREQMODWithPreviewClientHandler extends AbstractHandler {

		@Override
		public boolean doMessageReceived(ChannelHandlerContext context, MessageEvent event, Channel channel) throws Exception {
			Object msg = event.getMessage();
			if(msg instanceof IcapResponse) {
				IcapResponse response = (IcapResponse)msg;
				DataMockery.assertCreateREQMODWithPreviewAnnouncement204Response(response);
			} else {
				fail("unexpected msg instance [" + msg.getClass().getCanonicalName() + "]");
			}
			return true;
		}
	}
	
	private void sendREQMODWithPreview(PipelineType type) {
		try {
		runSocketTest(new SendREQMODWithPreviewServerHandler(),new SendREQMODWithPreviewClientHandler(),new Object[]{DataMockery.createREQMODWithPreviewAnnouncementIcapMessage(),
			DataMockery.createREQMODWithPreviewIcapChunk(),DataMockery.createREQMODWithPreviewLastIcapChunk()},type);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("encoding error");
		}
	}
	
	@Test
	public void sendREQMODWithPreviewThroughClassicPipeline() {
		sendREQMODWithPreview(PipelineType.CLASSIC);
	}
	
	@Test
	public void sendREQMODWithPreviewThroughCodecPipeline() {
		sendREQMODWithPreview(PipelineType.CODEC);
	}
	
	@Test
	public void sendREQMODWithPreviewThroughTricklePipeline() {
		sendREQMODWithPreview(PipelineType.TRICKLE);
	}
	
	@Test
	public void sendREQMODWithPreviewThroughAggregatorPipleline() {
		try {
			runSocketTest(new SendREQMODWithPreviewAggregatorServerHandler(),new SendREQMODWithPreviewClientHandler(),new Object[]{DataMockery.createREQMODWithPreviewAnnouncementIcapMessage(),
				DataMockery.createREQMODWithPreviewIcapChunk(),DataMockery.createREQMODWithPreviewLastIcapChunk()},PipelineType.AGGREGATOR);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				fail("encoding error");
			}
	}
	
	// TODO Test preview, 100 Continue with and without chunk aggregator
}

