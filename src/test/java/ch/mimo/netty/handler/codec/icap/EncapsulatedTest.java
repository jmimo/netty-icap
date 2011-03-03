package ch.mimo.netty.handler.codec.icap;

import org.junit.Test;

import junit.framework.Assert;

public class EncapsulatedTest extends Assert {

	@Test
	public void testSimpleValueParsing() {
		String parameter = "req-hdr=0, res-hdr=45, req-body=124";
		Encapsulated encapsulated = new Encapsulated(IcapMethod.RESPMOD,parameter);
		assertEquals("req-hdr value is wrong",0,encapsulated.getEncapsulatedValue(Encapsulated.REQHDR));
		assertEquals("res-hdr value is wrong",45,encapsulated.getEncapsulatedValue(Encapsulated.RESHDR));
		assertEquals("req-body value is wrong",124,encapsulated.getEncapsulatedValue(Encapsulated.REQBODY));
	}
	
	@Test
	public void testParameterValidation() {
		String parameter = "req-hdr=0, req-body=124";
		boolean error = false;
		try {
		new Encapsulated(IcapMethod.RESPMOD,parameter);
		} catch(Error e) {
			error = true;
		}
		assertTrue("Validation error did not occur",error);
	}
	
	@Test
	public void testWhitespaceGap() {
		String parameter = "req-hdr=0,  res-hdr=45, req-body=124";
		Encapsulated encapsulated = new Encapsulated(IcapMethod.RESPMOD,parameter);
		assertEquals("req-hdr value is wrong",0,encapsulated.getEncapsulatedValue(Encapsulated.REQHDR));
		assertEquals("res-hdr value is wrong",45,encapsulated.getEncapsulatedValue(Encapsulated.RESHDR));
		assertEquals("req-body value is wrong",124,encapsulated.getEncapsulatedValue(Encapsulated.REQBODY));
	}
	
	@Test
	public void testGarbledString() {
		String parameter = "   req-hdr=0; req-body=124, Whaterver   ";
		boolean error = false;
		try {
		new Encapsulated(IcapMethod.RESPMOD,parameter);
		} catch(Error e) {
			error = true;
		}
		assertTrue("Validation error did not occur",error);
	}
}
