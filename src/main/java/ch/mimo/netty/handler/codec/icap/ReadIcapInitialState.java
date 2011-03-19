package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;


public class ReadIcapInitialState extends State {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		// NO-OP
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		String[] initialLine = IcapDecoderUtil.splitInitialLine(IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength));
		if(initialLine != null && initialLine.length == 3) {
			icapMessageDecoder.message = icapMessageDecoder.createMessage(initialLine);
		}
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		if(icapMessageDecoder.message == null) {
			return StateEnum.SKIP_CONTROL_CHARS;
		}
		return StateEnum.READ_ICAP_HEADER_STATE;
	}
}
