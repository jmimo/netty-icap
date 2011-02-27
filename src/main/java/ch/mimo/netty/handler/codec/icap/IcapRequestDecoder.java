package ch.mimo.netty.handler.codec.icap;

public class IcapRequestDecoder extends IcapMessageDecoder {

	public IcapRequestDecoder() {
		super();
	}

	public IcapRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
		super(maxInitialLineLength, maxHeaderSize, maxChunkSize);
	}

	@Override
	public boolean isDecodingRequest() {
		return true;
	}

	@Override
	public IcapMessage createMessage() {
		return new DefaultIcapMessage(IcapVersion.ICAP_1_0);
	}
}
