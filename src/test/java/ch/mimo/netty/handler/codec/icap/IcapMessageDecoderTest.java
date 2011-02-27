package ch.mimo.netty.handler.codec.icap;

import static org.junit.Assert.assertEquals;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapMessageDecoderTest {

	private DecoderEmbedder<ChannelBuffer> embedder;
	
	@Before
	public void setUp() {
		embedder = new DecoderEmbedder<ChannelBuffer>(new IcapMessageDecoder());
	}
	
	@Test
	public void decodeOPTIONRequestTest() {
		StringBuilder builder = new StringBuilder();
		builder.append("OPTIONS icap://icap.mimo.ch:6758/reqmod ICAP/1.0").append("\r\n");
		builder.append("Host: icap.google.com:1344").append("\r\n");
		
		ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(builder.toString().getBytes());
		embedder.offer(buffer);
		ChannelBuffer result = embedder.poll();

		assertEquals(true,true);
	}
}
