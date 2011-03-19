package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class OptionsRequestActionState extends State<Object> {

	public OptionsRequestActionState() {
	}

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
		if(icapMessageDecoder.message.getEncapsulatedHeader() == null) {
			throw new IllegalArgumentException("This state requires a valid Encapsulation header instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws Exception {
		if(icapMessageDecoder.message.getEncapsulatedHeader().containsEntry(EntryName.OPTBODY)) {
			return StateReturnValue.createIrrelevantResult();
		}
		return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		if(icapMessageDecoder.message.getEncapsulatedHeader().containsEntry(EntryName.OPTBODY)) {
			// TODO return body processing state
		}
		return null;
	}

}
