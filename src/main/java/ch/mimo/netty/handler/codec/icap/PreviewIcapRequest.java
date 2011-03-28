package ch.mimo.netty.handler.codec.icap;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class PreviewIcapRequest extends DefaultIcapRequest implements IcapRequestWithPreview {
	
	private List<IcapChunk> previewChunks;
	
	public PreviewIcapRequest(HttpVersion icapVersion, HttpMethod method, String uri) {
		super(icapVersion,method,uri);
		// TODO remove literal
		super.addHeader("Preview","0");
		previewChunks = new ArrayList<IcapChunk>();
	}

	@Override
	public void addPreviewChunk(IcapChunk chunk) {
		int preview = Integer.parseInt(super.getHeader("Preview"));
		preview += chunk.getContent().readableBytes();
		super.removeHeader("Preview");
		super.addHeader("Preview",Integer.toString(preview));
		previewChunks.add(chunk);
	}

	@Override
	public IcapChunk[] getPreviewChunks() {
		return previewChunks.toArray(new IcapChunk[]{});
	}
}
