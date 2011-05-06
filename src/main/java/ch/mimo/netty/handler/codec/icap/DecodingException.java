package ch.mimo.netty.handler.codec.icap;

/**
 * Root decoding exception
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class DecodingException extends Exception {
	
	private static final long serialVersionUID = 1318955320625997060L;

	
	public DecodingException() {
		super();
	}

	public DecodingException(String message, Throwable cause) {
		super(message, cause);
	}

	public DecodingException(String message) {
		super(message);
	}

	public DecodingException(Throwable cause) {
		super(cause);
	}
}
