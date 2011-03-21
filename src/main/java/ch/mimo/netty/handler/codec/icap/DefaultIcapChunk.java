package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;

public class DefaultIcapChunk extends DefaultHttpChunk implements IcapChunk {

	private boolean preview;
	private boolean earlyTerminated;
	
	public DefaultIcapChunk(ChannelBuffer content) {
		super(content);
	}

	public DefaultIcapChunk(ChannelBuffer content, boolean isPreview, boolean isEarlyTerminated) {
		this(content);
		preview = isPreview;
		earlyTerminated = isEarlyTerminated;
	}
	
	@Override
	public boolean isPreviewChunk() {
		return preview;
	}

	@Override
	public boolean wasEarlyTerminated() {
		return earlyTerminated;
	}
}
