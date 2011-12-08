package ch.mimo.netty.handler.codec.icap;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapRequestDecoderPipelineTest extends AbstractIcapTest {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapRequestDecoder(),new IcapChunkAggregator(4012));
	}
	
	@Test
	public void decodeREQMODRequestWithoutBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBody());
		IcapRequest request = (IcapRequest)embedder.poll();
		assertNotNull("The request object is null",request);
		DataMockery.assertCreateREQMODWithGetRequestNoBody(request);
		assertTrue("body found",request.getHttpRequest().getContent().readableBytes() <= 0);
	}
	
	@Test
	public void decodeREQMODRequestWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		IcapRequest request = (IcapRequest)embedder.poll();
		assertNotNull("The request object is null",request);
		DataMockery.assertCreateREQMODWithTwoChunkBody(request);
		assertEquals("body has wrong size",109,request.getHttpRequest().getContent().readableBytes());
	}
	
	@Test
	public void decodeREQMODRequestWithBodyTwice() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		IcapRequest request = (IcapRequest)embedder.poll();
		assertNotNull("The request object is null",request);
		DataMockery.assertCreateREQMODWithTwoChunkBody(request);
		assertEquals("body has wrong size",109,request.getHttpRequest().getContent().readableBytes());
		embedder.offer(DataMockery.createREQMODWithTwoChunkBody());
		request = (IcapRequest)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBody(request);
		assertEquals("body has wrong size",109,request.getHttpRequest().getContent().readableBytes());
	}
}
