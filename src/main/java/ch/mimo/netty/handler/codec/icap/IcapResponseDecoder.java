package ch.mimo.netty.handler.codec.icap;

public class IcapResponseDecoder extends IcapMessageDecoder {

	public IcapResponseDecoder() {
		super();
	}

	public IcapResponseDecoder (int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
		super(maxInitialLineLength, maxIcapHeaderSize, maxHttpHeaderSize, maxChunkSize);
	}
	
	@Override
	protected IcapMessage createMessage(String[] initialLine) {
		return new DefaultIcapResponse(IcapVersion.valueOf(initialLine[0]),IcapResponseStatus.fromCode(initialLine[1]));
	}

	@Override
	public boolean isDecodingResponse() {
		return true;
	}
}
