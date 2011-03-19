package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunk;

public class PreviewState extends State {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		// TODO remove literal
		int previewSize = Integer.parseInt(icapMessageDecoder.message.getHeader("Preview"));
		// TODO deal with ioef!
		if(buffer.readableBytes() < previewSize) {
			throw new CorruptedFrameException("The Preview header indicates that [" + previewSize + "] bytes are available. The buffer has only [" + buffer.readableBytes() + "] byte available");
		}
		HttpChunk chunk = new DefaultHttpChunk(buffer.readBytes(previewSize));
		icapMessageDecoder.message.setPreview(chunk);
		return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		// TODO go to body processing form here.
		return null;
	}

}
