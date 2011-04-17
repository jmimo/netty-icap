package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DuplicatedChannelBuffer;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

public class AnaylsisChannelBuffer extends DuplicatedChannelBuffer {

	private static final InternalLogger LOG = InternalLoggerFactory.getInstance(AnaylsisChannelBuffer.class);
	
	public AnaylsisChannelBuffer(ChannelBuffer buffer) {
		super(buffer);
	}
	
	@Override
	public void getBytes(int index, byte[] dst) {
//		ChannelBuffer buffer = unwrap();
		byte[] destination = new byte[dst.length]; 
		super.getBytes(index,destination);
		StringBuilder builder = new StringBuilder();
		for(byte bite : destination) {
			builder.append(convertToString(bite));
		}
		LOG.debug(builder.toString());
		dst = destination;
	}

	@Override
	public byte getByte(int index) {
		byte bite = super.getByte(index);
		LOG.debug(convertToString(bite));
		return bite;
	}

	@Override
	public void getBytes(int index, byte[] dst, int dstIndex, int length) {
		byte[] destination = new byte[dst.length]; 
		super.getBytes(index,destination,dstIndex,length);
		StringBuilder builder = new StringBuilder();
		for(byte bite : destination) {
			builder.append(convertToString(bite));
		}
		LOG.debug(builder.toString());
		dst = destination;
	}

	private String convertToString(byte bite) {
		if(bite == IcapCodecUtil.CR) {
			return "[CR]";
		} else if(bite == IcapCodecUtil.LF) {
			return "[LF]";
		} else {
			return Character.toString((char)bite);
		}
	}
}
