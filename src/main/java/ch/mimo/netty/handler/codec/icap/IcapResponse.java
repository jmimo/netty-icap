package ch.mimo.netty.handler.codec.icap;

public interface IcapResponse extends IcapMessage {

	void setIcapResponseStatus(IcapResponseStatus status);
	
	IcapResponseStatus getIcapResponseStatus();
}
