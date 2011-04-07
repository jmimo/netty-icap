package ch.mimo.netty.handler.codec.icap;

public interface IcapResponse extends IcapMessage {

	void setStatus(IcapResponseStatus status);
	
	IcapResponseStatus getStatus();
}
