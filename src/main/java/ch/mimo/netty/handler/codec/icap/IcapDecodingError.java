package ch.mimo.netty.handler.codec.icap;

public class IcapDecodingError extends Error {

	private static final long serialVersionUID = 485693202925398675L;
	
	public IcapDecodingError() {
		super();
	}

	public IcapDecodingError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IcapDecodingError(String arg0) {
		super(arg0);
	}

	public IcapDecodingError(Throwable arg0) {
		super(arg0);
	}
}
