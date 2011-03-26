package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

public class IcapRequestEncoder extends IcapMessageEncoder {

	public IcapRequestEncoder() {
		super();
	}
	
	@Override
	protected int encodeInitialLine(ChannelBuffer buffer, IcapMessage message) throws Exception {
		IcapMessage request = (IcapMessage) message;
		// TODO replace ASCII literal
		int index = buffer.readableBytes();
        buffer.writeBytes(request.getMethod().toString().getBytes("ASCII"));
        buffer.writeByte(IcapCodecUtil.SP);
        buffer.writeBytes(request.getUri().getBytes("ASCII"));
        buffer.writeByte(IcapCodecUtil.SP);
        buffer.writeBytes(request.getProtocolVersion().toString().getBytes("ASCII"));
        buffer.writeByte(IcapCodecUtil.CR);
        buffer.writeByte(IcapCodecUtil.LF);
        return buffer.readableBytes() - index;
	}

}
