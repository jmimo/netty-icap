package ch.mimo.netty.handler.codec.icap;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapResponseDecoderTest extends AbstractIcapTest {

	private DecoderEmbedder<Object> embedder;
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		embedder = new DecoderEmbedder<Object>(new IcapResponseDecoder());
	}
	
	@Test
	public void decodeOPTIONSResponse() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createOPTIONSResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertOPTIONSResponse(response);
	}
	
	@Test
	public void decodeREQMODResponseWithGetRequestNoBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithGetRequestResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertREQMODWithGetRequestResponse(response);
	}
	
	@Test
	public void decodeRESPMODWithGetRequestNoBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertRESPMODWithGetRequestNoBodyResponse(response);
	}

	@Test
	public void decodeREQMODResponseWithGetRequestAndBody() throws UnsupportedEncodingException {
		embedder.offer(DataMockery.createREQMODWithImplicitTwoChunkBodyResponse());
		IcapResponse response = (IcapResponse)embedder.poll();
		doOutput(response.toString());
		DataMockery.assertCreateREQMODWithImplicitTwoChunkBodyResponse(response);
		IcapChunk chunk1 = (IcapChunk)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBodyFirstChunk(chunk1);
		IcapChunk chunk2 = (IcapChunk)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBodySecondChunk(chunk2);
		IcapChunk chunk3 = (IcapChunk)embedder.poll();
		DataMockery.assertCreateREQMODWithTwoChunkBodyThirdChunk(chunk3);
	}
	
	// TODO create RESPMOD with body response test
}
