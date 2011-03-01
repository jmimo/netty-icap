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
	protected IcapMessage createMessage(String[] initialLine) throws Exception {
		return new DefaultIcapMessage(IcapVersion.valueOf(initialLine[2]),IcapMethod.valueOf(initialLine[0]),initialLine[1]);
	}
}
