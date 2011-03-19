package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunk;

public class ReadChunkedContentAsChunksState extends State<Object> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		HttpChunk chunk = null;
		if(icapMessageDecoder.currentChunkSize > icapMessageDecoder.maxChunkSize) {
			chunk = new DefaultHttpChunk(buffer.readBytes(icapMessageDecoder.maxChunkSize));
			icapMessageDecoder.currentChunkSize -= icapMessageDecoder.maxChunkSize;
		} else {
			chunk = new DefaultHttpChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
			icapMessageDecoder.currentChunkSize = 0;
		}
		
		if(chunk.isLast()) {
			icapMessageDecoder.currentChunkSize = 0;
			return StateReturnValue.createRelevantResult(new Object[]{chunk,HttpChunk.LAST_CHUNK}); 
		}
		return StateReturnValue.createRelevantResult(chunk);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		if(icapMessageDecoder.currentChunkSize == 0) {
			return StateEnum.READ_CHUNK_DELIMITER_STATE;
		}
		return StateEnum.READ_CHUNKED_CONTENT_AS_CHUNKS_STATE;
	}

}
