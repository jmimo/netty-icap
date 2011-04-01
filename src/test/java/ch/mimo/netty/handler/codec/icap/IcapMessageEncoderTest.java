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
		assertResponse(DataMockery.createOPTIONSRequest(),request);
	}
	
	@Test
	public void encodeREQMODRequestWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithGetRequestNoBody(),request);
	}
	
	@Test
	public void encodeRESMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createRESPMODWithGetRequestNoBody(),request);
	}
	
	@Test
	public void encodeREQMODWithTwoChunkBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyAnnouncement(),request);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkOne());
		String chunkOne = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTowChunkBodyChunkOne(),chunkOne);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkTwo());
		String chunkTwo = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBodyChunkTwo(),chunkTwo);
		embedder.offer(DataMockery.createREQMODWithTwoChunkBodyIcapChunkThree());
		String chunkThree = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithTwoChunkBudyChunkThree(),chunkThree);
	}
	
	@Test
	public void encodeREQMODWithPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithPreviewAnnouncementIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithPreviewAnnouncement(),request);
		embedder.offer(DataMockery.createREQMODWithPreviewIcapChunk());
		String previewChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithPreviewChunk(),previewChunk);
		embedder.offer(DataMockery.createREQMODWithPreviewLastIcapChunk());
		String lastChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithPreviewLastChunk(),lastChunk);
	}
	
	@Test
	public void encodeREQMODWithEarlyTerminatedPreview() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreviewAnnouncementIcapMessage());
		String request = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithEarlyTerminatedPreviewAnnouncement(),request);
		
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreviewIcapChunk());
		String previewChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithEarlyTerminatedPreviewChunk(),previewChunk);
		embedder.offer(DataMockery.createREQMODWithEarlyTerminatedPreviewLastIcapChunk());
		String lastChunk = getBufferContent(embedder.poll());
		assertResponse(DataMockery.createREQMODWithEarlyTerminatedPreviewLastChunk(),lastChunk);
	}

	// TODO trailing headers
	// TODO options request with body
	
	private String getBufferContent(Object object) {
		assertNotNull("poll returned null",object);
		assertTrue("returned object from embedder is not of type ChannelBuffer",object instanceof ChannelBuffer);
		ChannelBuffer buffer = (ChannelBuffer)object;
		return buffer.toString(Charset.defaultCharset());
	}
	
	private void assertResponse(ChannelBuffer expected, String request) {
		assertEquals("encoded request is wrong",getBufferContent(expected),request);
	}
}
