package ch.mimo.netty.handler.codec.icap.socket;


public interface Handler {

	boolean hasException();
	
	Throwable getExceptionCause();
	
	boolean isProcessed();
	
	void close();
}
