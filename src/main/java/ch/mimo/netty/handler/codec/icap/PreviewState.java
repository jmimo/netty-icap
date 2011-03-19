package ch.mimo.netty.handler.codec.icap;

import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunk;

public class PreviewState extends State<Object> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		// TODO remove literal
		int previewSize = Integer.parseInt(icapMessageDecoder.message.getHeader("Preview"));
		int readable = buffer.readableBytes();
		ChannelBuffer preview = null;
		
		if(readable > previewSize) {
			preview = buffer.readBytes(previewSize);
		} else {
			preview = buffer.readBytes(readable);
			byte[] end = new byte[IcapCodecUtil.IEOF_SEQUENCE.length];
			preview.getBytes(preview.readableBytes() - IcapCodecUtil.IEOF_SEQUENCE.length,end);
			if(Arrays.equals(IcapCodecUtil.IEOF_SEQUENCE,end)) {
				preview = preview.copy(0,preview.readableBytes() - IcapCodecUtil.IEOF_SEQUENCE.length);
			} else {
				preview = null;
			}
		}
		
		if(preview != null) {
			HttpChunk chunk = new DefaultHttpChunk(buffer.readBytes(previewSize));
			icapMessageDecoder.message.setPreview(chunk);
			return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
		}
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		if(icapMessageDecoder.message.getPreview() == null) {
			return StateEnum.PREVIEW_STATE;
		}
		return StateEnum.READ_CHUNK_SIZE_STATE;
	}
}
