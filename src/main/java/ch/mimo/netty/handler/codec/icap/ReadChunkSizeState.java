package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

public class ReadChunkSizeState extends State<Integer> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		String line = IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength);
		int chunkSize = IcapDecoderUtil.getChunkSize(line);
		
		return StateReturnValue.createIrrelevantResultWithDecisionInformation(chunkSize);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Integer decisionInformation) throws Exception {
		if(decisionInformation >= icapMessageDecoder.maxChunkSize) {
			return StateEnum.READ_CHUNKED_CONTENT_AS_CHUNKS_STATE;
		}
		if(decisionInformation == 0) {
			return StateEnum.READ_TRAILING_HEADERS_STATE;
		}
		return StateEnum.READ_CHUNK_STATE;
	}

}
