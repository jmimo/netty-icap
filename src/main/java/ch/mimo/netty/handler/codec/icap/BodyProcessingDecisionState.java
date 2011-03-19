package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

public class BodyProcessingDecisionState extends State {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		if(icapMessageDecoder.message.isPreview()) {
			return StateReturnValue.createIrrelevantResult();
		}
		return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		if(icapMessageDecoder.message.isPreview()) {
			// TODO process preview
		}
		// TODO process body
		return null;
	}

}
