package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

public class ReadChunkDelimiterState extends State<Object> {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
        for (;;) {
            byte next = buffer.readByte();
            if (next == IcapCodecUtil.CR) {
                if (buffer.readByte() == IcapCodecUtil.LF) {
                    return StateReturnValue.createIrrelevantResult();
                }
            } else if (next == IcapCodecUtil.LF) {
                return StateReturnValue.createIrrelevantResult();
            }
        }
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		return StateEnum.READ_CHUNK_SIZE_STATE;
	}

}
