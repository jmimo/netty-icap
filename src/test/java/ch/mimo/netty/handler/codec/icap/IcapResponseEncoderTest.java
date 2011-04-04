package ch.mimo.netty.handler.codec.icap;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapResponseEncoderTest extends AbstractEncoderTest {

	private EncoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() {
		embedder = new EncoderEmbedder<Object>(new IcapResponseEncoder());
	}
	
	@Test
	public void encode100ContinueResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.create100ContinueIcapResponse());
		String response = getBufferContent(embedder.poll());
		assertResponse(DataMockery.create100ContinueResponse(),response);
	}
}
