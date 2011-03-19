package ch.mimo.netty.handler.codec.icap;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class ReadIcapHeaderState extends State {

	public ReadIcapHeaderState() {
	}

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		// TODO validate required elements
		// message cannot be null
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		List<String[]> headerList = IcapDecoderUtil.readHeaders(buffer,icapMessageDecoder.maxIcapHeaderSize);
		icapMessageDecoder.message.clearHeaders();
		for(String[] header : headerList) {
			icapMessageDecoder.message.addHeader(header[0],header[1]);
		}
		// validate mandatory icap headers
		if(!icapMessageDecoder.message.containsHeader(IcapHeaders.Names.HOST)) {
			throw new Error("Mandatory ICAP message header [Host] is missing");
		}
		if(!icapMessageDecoder.message.containsHeader(IcapHeaders.Names.ENCAPSULATED)) {
			throw new Error("Mandatory ICAP message header [Encapsulated] is missing");
		}
		Encapsulated encapsulated = Encapsulated.parseHeader(icapMessageDecoder.message.getHeader(IcapHeaders.Names.ENCAPSULATED));
		encapsulated.setBufferOffsetIndex(buffer.readerIndex());
		icapMessageDecoder.message.setEncapsulatedHeader(encapsulated);
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		IcapMessage message = icapMessageDecoder.message;
		Encapsulated encapsulated = message.getEncapsulatedHeader();
		if(message.getMethod().equals(IcapMethod.OPTIONS)) {
			return StateEnum.OPTIONS_REQUEST_ACTION_STATE;
		}
		EntryName entry = encapsulated.getNextEntry();
		if(entry.equals(EntryName.REQHDR)) {
			return StateEnum.READ_HTTP_REQUEST_INITIAL_AND_HEADERS;
		}
		if(entry.equals(EntryName.RESHDR)) {
			return StateEnum.READ_HTTP_RESPONSE_INITIAL_AND_HEADERS;
		}
		if(entry.equals(EntryName.REQBODY) | entry.equals(EntryName.RESBODY)) {
			return StateEnum.BODY_PROCESSING_DECISION_STATE;
		}
		return null;
	}

}
