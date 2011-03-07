package ch.mimo.netty.handler.codec.icap;


public class IcapRequestDecoder extends IcapMessageDecoder {

	public IcapRequestDecoder() {
		super();
	}

	public IcapRequestDecoder(int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
		super(maxInitialLineLength, maxIcapHeaderSize, maxHttpHeaderSize, maxChunkSize);
	}

	@Override
	public boolean isDecodingRequest() {
		return true;
	}

	@Override
	protected IcapMessage createMessage(String[] initialLine) throws Exception {
		if(initialLine != null) {
			if(initialLine.length == 3) {
				return new DefaultIcapMessage(IcapVersion.valueOf(initialLine[2]),IcapMethod.valueOf(initialLine[0]),initialLine[1]);
			}
		}
		// TODO replace with specific exception.
		throw new Exception("initial line array has wrong size");
	}
}
