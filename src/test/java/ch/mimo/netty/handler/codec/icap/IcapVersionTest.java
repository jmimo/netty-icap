package ch.mimo.netty.handler.codec.icap;

import org.junit.Test;

public class IcapVersionTest extends AbstractIcapTest {

	@Test
	public void getValueOfString() {
		assertEquals("no version found",IcapVersion.ICAP_1_0,IcapVersion.valueOf("ICAP/1.0"));
	}
	
	@Test
	public void getValueFromNull() {
		boolean exception = false;
		try {
			IcapVersion.valueOf(null);
		} catch(NullPointerException npe) {
			exception = true;
		}
		assertTrue("no npe was thrown",exception);
	}
	
	@Test
	public void getValueFromUnknownString() {
		boolean exception = false;
		try {
			IcapVersion.valueOf("ICAP/1.X");
		} catch(IllegalArgumentException iae) {
			exception = true;
		}
		assertTrue("no iae was thrown",exception);
	}
}
