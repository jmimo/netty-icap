package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.handler.codec.http.HttpChunk;

public interface IcapChunk extends HttpChunk {

	public boolean isPreviewChunk();
	
	public boolean wasEarlyTerminated();
}
