package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;


public class SkipControlCharsState extends State<Object> {

	public SkipControlCharsState() {
	}

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		//NO-OP
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		IcapDecoderUtil.skipControlCharacters(buffer);
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		return StateEnum.READ_ICAP_INITIAL_STATE;
	}
}
