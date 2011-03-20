package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunk;

public class ReadChunkState extends State<Boolean> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		HttpChunk chunk = new DefaultHttpChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
		if(chunk.isLast()) {
			return StateReturnValue.createRelevantResultWithDecisionInformation(new Object[]{chunk,HttpChunk.LAST_CHUNK},Boolean.TRUE);
		}
		return StateReturnValue.createRelevantResultWithDecisionInformation(chunk,Boolean.FALSE);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Boolean decisionInformation) throws Exception {
		if(decisionInformation) {
			return null;
		}
		return StateEnum.READ_CHUNK_DELIMITER_STATE;
	}

}
