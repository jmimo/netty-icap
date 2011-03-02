package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.handler.codec.frame.TooLongFrameException;

public class SizeDelimiter {

	private int counter = 0;
	private int limit;
	private String errorMessage;
	
	public SizeDelimiter(int limit) {
		this.limit = limit;
		this.errorMessage = "limit exeeded by: ";
	}
	
	public SizeDelimiter( int limit, String errorMessage) {
		this(limit);
		this.errorMessage = errorMessage;
	}
	
	public synchronized void increment(int count) throws TooLongFrameException {
		counter += count;
		checkLimit();
	}
	
	public void increment() throws TooLongFrameException {
		this.increment(1);
	}
	
	public synchronized void decrement(int count) throws TooLongFrameException {
		counter -= count;
		checkLimit();
	}
	
	public void decrement() throws TooLongFrameException {
		this.decrement(1);
	}
	
	public int getSize() {
		return counter;
	}
	
	private void checkLimit() throws TooLongFrameException {
		if(counter >= limit) {
			throw new TooLongFrameException(errorMessage + "[" + (counter - limit) + "] counts");
		}
	}
}
