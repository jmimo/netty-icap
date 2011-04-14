package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

public enum IcapResponseStatus {
	CONTINUE(100,"Continue"),
	OK(200,"OK"),
	NO_CONTENT(204,"No Content");
	
	private String status;
	private int code;
	
	IcapResponseStatus(int code, String status) {
		this.code = code;
		this.status = status;
	}
	
	public int getCode() {
		return code;
	}
	
	public void toResponseInitialLineValue(ChannelBuffer buffer) {
		buffer.writeBytes(Integer.toString(code).getBytes(IcapCodecUtil.ASCII_CHARSET));
		buffer.writeByte(IcapCodecUtil.SP);
		buffer.writeBytes(status.getBytes(IcapCodecUtil.ASCII_CHARSET));
	}
	
	public static IcapResponseStatus fromCode(String code) {
		for(IcapResponseStatus status : IcapResponseStatus.values()) {
			if(Integer.toString(status.getCode()).equalsIgnoreCase(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Unknown Icap response code [" + code + "]");
	}
}
