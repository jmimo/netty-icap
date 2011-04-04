package ch.mimo.netty.handler.codec.icap;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class IcapResponseEncoderTest extends Assert {

	private EncoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() {
		embedder = new EncoderEmbedder<Object>(new IcapResponseEncoder());
	}
	
	@Test
	public void encode100ContinueResponse() {
		embedder.offer(DataMockery.create100ContinueResponse());
		String response = getBufferContent(embedder.poll());
		System.out.println(response);
	}
	
	// TODO make abstract class with utilites for testing
	private String getBufferContent(Object object) {
		assertNotNull("poll returned null",object);
		assertTrue("returned object from embedder is not of type ChannelBuffer",object instanceof ChannelBuffer);
		ChannelBuffer buffer = (ChannelBuffer)object;
		return buffer.toString(Charset.defaultCharset());
	}
}
