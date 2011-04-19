package ch.mimo.netty.handler.codec.icap;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapChunkSeparatorTest extends AbstractIcapTest {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapChunkSeparator(20));
	}
	
	@Test
	public void separateREQMODWithGetRequestAndData() {
		embedder.offer(DataMockery.createREQMODWithGetRequestAndDataIcapMessage());
		IcapMessage message = (IcapMessage)embedder.poll();
		assertNotNull("message was null",message);
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 1 was null",chunk1);
		assertEquals("chunk 1 content is wrong","This is data that wa",chunk1.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 2 was null",chunk2);
		assertEquals("chunk 2 content is wrong","s returned by an ori",chunk2.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		assertNotNull("chunk 3 was null",chunk3);
		assertEquals("chunk 3 content is wrong","gin server.",chunk3.getContent().toString(IcapCodecUtil.ASCII_CHARSET));
		IcapChunkTrailer trailer = (IcapChunkTrailer)embedder.poll();
		assertNotNull("chunk trailer was null",trailer);
	}
	
	// TODO test response content
	// TODO test icap response
	// TODO test preview
	// TODO test preview with early termination
	// TODO test options body
}
