package ch.mimo.netty.handler.codec.icap;

import junit.framework.Assert;

import org.junit.Test;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class EncapsulatedTest extends Assert {

	@Test
	public void testSimpleValueParsing() {
		String parameter = "req-hdr=0, res-hdr=45, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertTrue("req-hdr value is missing",encapsulated.containsEntry(EntryName.REQHDR));
		assertTrue("res-hdr value is missing",encapsulated.containsEntry(EntryName.RESHDR));
		assertTrue("req-body value is missing",encapsulated.containsEntry(EntryName.REQBODY));
	}
	
	@Test
	public void testWhitespaceGap() {
		String parameter = "req-hdr=0,  res-hdr=45, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertTrue("req-hdr value is missing",encapsulated.containsEntry(EntryName.REQHDR));
		assertTrue("res-hdr value is missing",encapsulated.containsEntry(EntryName.RESHDR));
		assertTrue("req-body value is missing",encapsulated.containsEntry(EntryName.REQBODY));
	}
	
	@Test
	public void testIteratorWithWrongSequence() {
		String parameter = "res-hdr=45, req-hdr=0, req-body=124";
		Encapsulated encapsulated = Encapsulated.parseHeader(parameter);
		assertTrue("req-hdr value is missing",encapsulated.containsEntry(EntryName.REQHDR));
		assertTrue("res-hdr value is missing",encapsulated.containsEntry(EntryName.RESHDR));
		assertTrue("req-body value is missing",encapsulated.containsEntry(EntryName.REQBODY));
		EntryName reqhdr = encapsulated.getNextEntry();
		assertEquals("req-hdr was expected",EntryName.REQHDR,reqhdr);
		encapsulated.setProcessed(reqhdr);
		EntryName reshdr = encapsulated.getNextEntry();
		assertEquals("res-hdr was expected",EntryName.RESHDR,reshdr);
		encapsulated.setProcessed(reshdr);
		EntryName reqbody = encapsulated.getNextEntry();
		assertEquals("reqbody was expected",EntryName.REQBODY,reqbody);
		encapsulated.setProcessed(reqbody);
		assertNull("null was expected after last entry is processed",encapsulated.getNextEntry());
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
