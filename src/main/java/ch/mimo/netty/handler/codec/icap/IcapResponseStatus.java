package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

public enum IcapResponseStatus {
	CONTINUE(100,"Continue"),
	OK(200,"OK");
	
	private String status;
	private int code;
	
	IcapResponseStatus(int code, String status) {
		this.code = code;
		this.status = status;
	}
	
	public void toResponseInitialLineValue(ChannelBuffer buffer) {
		buffer.writeBytes(status.getBytes(IcapCodecUtil.ASCII_CHARSET));
		buffer.writeByte(IcapCodecUtil.SP);
		buffer.writeBytes(Integer.toString(code).getBytes(IcapCodecUtil.ASCII_CHARSET));
	}
}
