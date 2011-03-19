package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;


public abstract class State<T extends Object> {
	
	public State() {
	}
	
	/**
	 * Preparation method
	 */
	public abstract void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception;
	
	/**
	 * execution method
	 * @return @see {@link StateReturnValue} that contains, dependent on the relevance a return value.
	 */
	public abstract StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception;
	
	/**
	 * Flow decision method
	 * @return has to return a valid next state. Can be itself.
	 */
	public abstract StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, T decisionInformation) throws Exception;
	
	public boolean equals(Object object) {
		if(object != null && object instanceof State<?>) {
			return object.getClass().getName().equals(this.getClass().getName());
		}
		return false;
	}
}
