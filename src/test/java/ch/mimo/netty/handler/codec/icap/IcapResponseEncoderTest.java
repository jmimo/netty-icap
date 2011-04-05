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
		doOutput(response);
		assertResponse(DataMockery.create100ContinueResponse(),response);
	}
	
	@Test
	public void encodeOPTIONSResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSIcapResponse());
		String response = getBufferContent(embedder.poll());
		doOutput(response);
		assertResponse(DataMockery.createOPTIONSResponse(),response);
	}
	
	@Test
	public void encodeOPTIONSResponseWithBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSIcapResponseWithBody());
		String response = getBufferContent(embedder.poll());
		doOutput(response);
		assertResponse(DataMockery.createOPTIONSResponseWithBody(),response);
		embedder.offer(DataMockery.createOPTIONSIcapChunk());
		String chunk1 = getBufferContent(embedder.poll());
		doOutput(chunk1);
		assertResponse(DataMockery.createOPTIONSChunk(),chunk1);
		embedder.offer(DataMockery.createOPTIONSLastIcapChunk());
		String lastChunk = getBufferContent(embedder.poll());
		doOutput(lastChunk);
		assertResponse(DataMockery.createOPTIONSLastChunk(),lastChunk);
	}
}
