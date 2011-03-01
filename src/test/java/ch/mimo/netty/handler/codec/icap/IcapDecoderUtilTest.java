package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.junit.Test;

import junit.framework.Assert;

public class IcapDecoderUtilTest extends Assert {

	@Test
	public void testControlCharacterSkipping() {
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer("  TESTLINE".getBytes());
		IcapDecoderUtil.skipControlCharacters(buffer);
		assertEquals("skipping of whitespaces has not worked",'T',(char)buffer.readByte());
	}
	
	@Test
	public void testReadLine() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(IcapCodecUtil.CRLF)).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		String line = null;
		try {
			line = IcapDecoderUtil.readLine(buffer,100);
		} catch (TooLongFrameException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("Line was not identified","REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0",line);
	}
	
	@Test
	public void testReadLineMaximumLengthReached() {
		StringBuilder builder = new StringBuilder("REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0").append(new String(IcapCodecUtil.CRLF)).append("NEW LINE");
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(builder.toString().getBytes());
		boolean exception = false;
		try {
			IcapDecoderUtil.readLine(buffer,42);
		} catch (TooLongFrameException e) {
			exception = true;
		}
		assertTrue("No maximum length reached exception was thrown!",exception);
	}
	
	@Test
	public void testFindNonWhitespace() {
		String line = "  TESTSTRING";
		int position = IcapDecoderUtil.findNonWhitespace(line,0);
		assertEquals("Position of first non whitespace is wrong",2,position);
	}
	
	@Test
	public void testFindFirstWhitespace() {
		String line = "TESTSTRING1 TESTSTRING2";
		int position = IcapDecoderUtil.findWhitespace(line,0);
		assertEquals("Position of first whitespace is wrong",11,position);
	}
	
	@Test
	public void testFindEndOfString() {
		StringBuilder builder = new StringBuilder("TESTSTRING1 TESTSTRING2").append("    ");
		int end = IcapDecoderUtil.findEndOfString(builder.toString());
		assertEquals("End of String position is wrong",23,end);
	}
	
	@Test
	public void testParseInitialLine() {
		String initialLine = "REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0";
		String[] params = IcapDecoderUtil.splitInitialLine(initialLine);
		assertEquals("Operation could not be identified","REQMOD",params[0]);
		assertEquals("Uri could not be identified","icap://icap.mimo.ch:1344/reqmod",params[1]);
		assertEquals("Protocol and Version could not be identified","ICAP/1.0",params[2]);
	}
	
	@Test
	public void testParseInitialLineWithTrailingWhitespaces() {
		String initialLine = "  REQMOD icap://icap.mimo.ch:1344/reqmod ICAP/1.0  ";
		String[] params = IcapDecoderUtil.splitInitialLine(initialLine);
		assertEquals("Operation could not be identified","REQMOD",params[0]);
		assertEquals("Uri could not be identified","icap://icap.mimo.ch:1344/reqmod",params[1]);
		assertEquals("Protocol and Version could not be identified","ICAP/1.0",params[2]);
	}
}
