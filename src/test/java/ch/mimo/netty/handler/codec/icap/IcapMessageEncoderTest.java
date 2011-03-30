package ch.mimo.netty.handler.codec.icap;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapMessageEncoderTest extends Assert {

	private EncoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() {
		embedder = new EncoderEmbedder<Object>(new IcapRequestEncoder());
	}
	
	@Test
	public void encodeREQMODRequestWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRQMODWithGetRequestNoBody());
		String request = getBufferContent(embedder.poll());
		assertEquals("encoded request is wrong",getBufferContent(DataMockery.createREQMODWithGetRequestNoBody()),request);
		System.out.println(request);
	}
	
	private String getBufferContent(Object object) {
		assertNotNull("poll returned null",object);
		assertTrue("returned object from embedder is not of type ChannelBuffer",object instanceof ChannelBuffer);
		ChannelBuffer buffer = (ChannelBuffer)object;
		return buffer.toString(Charset.defaultCharset());
	}
}
