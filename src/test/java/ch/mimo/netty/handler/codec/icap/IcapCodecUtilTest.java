package ch.mimo.netty.handler.codec.icap;

import org.junit.Test;

public class IcapCodecUtilTest extends AbstractIcapTest {

	@Test
	public void validateHeaderName() {
		boolean exception = false;
		try {
			IcapCodecUtil.validateHeaderName("Hel;lo");
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderNameWithInvalidCharacter() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)128);
			builder.append("World");
			IcapCodecUtil.validateHeaderName(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValue() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.CR);
			builder.append((char)IcapCodecUtil.CR);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithOneLineFeed() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.LF);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithOneCarriageReturn() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.CR);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithlastCharacterBeeingCarriageReturn() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.CR);
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithlastCharacterBeeingLineFeed() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.LF);
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithProbhibitedCharacter() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append('\f');
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithOtherProbhibitedCharacter() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)0x0b);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
}
