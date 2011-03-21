package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

public class ReadChunkState extends State<Object> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		// TODO when reading preview then the early terminater ieof has to be expected. Otherwise a full buffer readBytes will do.
		// TODO change HttpChunk to IcapChunk that carries the necessary ieof and preview flags.
		
		IcapChunk chunk = new DefaultIcapChunk(buffer.readBytes(icapMessageDecoder.currentChunkSize));
		if(chunk.isLast()) {
			return StateReturnValue.createRelevantResult(new Object[]{chunk,IcapChunk.LAST_CHUNK});
		}
		return StateReturnValue.createRelevantResult(chunk);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		return StateEnum.READ_CHUNK_DELIMITER_STATE;
	}

}
