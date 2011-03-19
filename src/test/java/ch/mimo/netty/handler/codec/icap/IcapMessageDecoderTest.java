package ch.mimo.netty.handler.codec.icap;

import junit.framework.Assert;

import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Before;
import org.junit.Test;

public class IcapMessageDecoderTest extends Assert {

	private DecoderEmbedder<IcapMessage> embedder;
	
	@Before
	public void setUp() {
		embedder = new DecoderEmbedder<IcapMessage>(new IcapRequestDecoder());
	}
	
	@Test
	public void decodeOPTIONRequestTest() {
		embedder.offer(DataMockery.createOPTIONSRequest());
		IcapMessage result = embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateOPTIONSRequest(result);
	}
	
	@Test
	public void stripPrefixingWhitespacesFromMessage() {
		embedder.offer(DataMockery.createWhiteSpacePrefixedOPTIONSRequest());
		IcapMessage result = embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateWhiteSpacePrefixedOPTIONSRequest(result);
	}
	
	@Test
	public void decodeREQMODRequestWithNullBody() {
		embedder.offer(DataMockery.createREQMODWithGetRequestNoBody());
		IcapMessage result = embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateREQMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBody() {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBody());
		IcapMessage result = embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBody(result);
	}
	
	@Test
	public void decodeRESPMODRequestWithNullBodyAndReverseRequestAlignement() {
		embedder.offer(DataMockery.createRESPMODWithGetRequestNoBodyAndReverseRequestAlignement());
		IcapMessage result = embedder.poll();
		assertNotNull("The decoded icap request instance is null",result);
		DataMockery.assertCreateRESPMODWithGetRequestNoBodyAndReverseRequestAlignement(result);
	}
	
//	@Test
//	public void decodeRESPMODRequestWithBody() {
//		embedder.offer(DataMockery.createRESPMODWithGetRequestAndBody());
//		IcapMessage result = embedder.poll();
//		assertNotNull("The decoded icap request instance is null",result);
//	}
}
