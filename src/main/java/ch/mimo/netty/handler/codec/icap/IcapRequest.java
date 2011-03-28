package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public interface IcapRequest extends IcapMessage {

	void setContentType(EntryName type);
	
	EntryName getContentType();
	
	void setContent(ChannelBuffer content);

	ChannelBuffer getContent();
}
