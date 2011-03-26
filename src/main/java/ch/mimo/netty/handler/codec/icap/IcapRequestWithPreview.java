package ch.mimo.netty.handler.codec.icap;

public interface IcapRequestWithPreview extends IcapRequest {

	void addPreviewChunk(IcapChunk chunk);
	
	IcapChunk[] getPreviewChunks();
}
