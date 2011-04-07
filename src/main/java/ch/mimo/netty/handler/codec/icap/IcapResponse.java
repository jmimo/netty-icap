package ch.mimo.netty.handler.codec.icap;

public interface IcapResponse extends IcapMessage {

	// TODO rename to set and get Status!
	void setIcapResponseStatus(IcapResponseStatus status);
	
	IcapResponseStatus getIcapResponseStatus();
}
