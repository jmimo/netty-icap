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
	public void encodeOPTIONSRequest() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSRequestIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertRequest(DataMockery.createOPTIONSRequest(),request);
	}
	
	@Test
	public void encodeREQMODRequestWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertRequest(DataMockery.createREQMODWithGetRequestNoBody(),request);
	}
	
	@Test
	public void encodeRESMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertRequest(DataMockery.createRESPMODWithGetRequestNoBody(),request);
	}
	
	@Test
	public void encodeREQMODWithTwoChunkBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertRequest(DataMockery.createREQMODWithTwoChunkBodyAnnouncement(),request);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		String chunkOne = getBufferContent(embedder.poll());
		assertRequest(DataMockery.createREQMODWithTowChunkBodyChunkOne(),chunkOne);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
		String chunkTwo = getBufferContent(embedder.poll());
		assertRequest(DataMockery.createREQMODWithTwoChunkBodyChunkTwo(),chunkTwo);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree());
		String chunkThree = getBufferContent(embedder.poll());
		assertRequest(DataMockery.createREQMODWithTwoChunkBudyChunkThree(),chunkThree);
	}
	
	private String getBufferContent(Object object) {
		assertNotNull("poll returned null",object);
		assertTrue("returned object from embedder is not of type ChannelBuffer",object instanceof ChannelBuffer);
		ChannelBuffer buffer = (ChannelBuffer)object;
		return buffer.toString(Charset.defaultCharset());
	}
	
	private void assertRequest(ChannelBuffer expected, String request) {
		assertEquals("encoded request is wrong",getBufferContent(expected),request);
	}
}
