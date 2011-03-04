package ch.mimo.netty.handler.codec.icap;

import junit.framework.Assert;

import org.junit.Test;

public class EncapsulatedTest extends Assert {

	@Test
	public void testSimpleValueParsing() {
		String parameter = "req-hdr=0, res-hdr=45, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertEquals("req-hdr value is wrong",0,encapsulated.getPosition(Encapsulated.REQHDR));
		assertEquals("res-hdr value is wrong",45,encapsulated.getPosition(Encapsulated.RESHDR));
		assertEquals("req-body value is wrong",124,encapsulated.getPosition(Encapsulated.REQBODY));
	}
	
	@Test
	public void testWhitespaceGap() {
		String parameter = "req-hdr=0,  res-hdr=45, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertEquals("req-hdr value is wrong",0,encapsulated.getPosition(Encapsulated.REQHDR));
		assertEquals("res-hdr value is wrong",45,encapsulated.getPosition(Encapsulated.RESHDR));
		assertEquals("req-body value is wrong",124,encapsulated.getPosition(Encapsulated.REQBODY));
	}
	
	@Test
	public void testGarbledString() {
		String parameter = "   req-hdr=0; req-body=124, Whaterver   ";
		boolean error = false;
		try {
			Encapsulated.parseHeader(parameter);
		} catch(Error e) {
			error = true;
		}
		assertTrue("Validation error did not occur",error);
	}
}
