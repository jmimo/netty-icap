package ch.mimo.netty.handler.codec.icap;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

import junit.framework.Assert;

public abstract class AbstractEncoderTest extends Assert {

	protected String getBufferContent(Object object) {
		assertNotNull("poll returned null",object);
		assertTrue("returned object from embedder is not of type ChannelBuffer",object instanceof ChannelBuffer);
		ChannelBuffer buffer = (ChannelBuffer)object;
		return buffer.toString(Charset.defaultCharset());
	}
	
	protected void assertResponse(ChannelBuffer expected, String request) {
		assertEquals("encoded request is wrong",getBufferContent(expected),request);
	}
}
