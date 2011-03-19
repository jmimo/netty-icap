package ch.mimo.netty.handler.codec.icap;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class ReadHttpRequestInitialAndHeadersState extends State {

	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		if(icapMessageDecoder.message == null) {
			throw new IllegalArgumentException("This state requires a valid IcapMessage instance");
		}
		if(icapMessageDecoder.message.getEncapsulatedHeader() == null) {
			throw new IllegalArgumentException("This state requires a valid Encapsulation header instance");
		}
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		String line = IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength);
		String[] initialLine = IcapDecoderUtil.splitInitialLine(line);
		HttpRequest message = new DefaultHttpRequest(HttpVersion.valueOf(initialLine[2]),HttpMethod.valueOf(initialLine[0]),initialLine[1]);
		icapMessageDecoder.message.setHttpRequest(message);
		List<String[]> headerList = IcapDecoderUtil.readHeaders(buffer,icapMessageDecoder.maxHttpHeaderSize);
		message.clearHeaders();
		for(String[] header : headerList) {
			message.addHeader(header[0],header[1]);
		}
		Encapsulated encapsulated = icapMessageDecoder.message.getEncapsulatedHeader();
		encapsulated.setProcessed(encapsulated.getNextEntry());
		if(encapsulated.getNextEntry() == null || encapsulated.getNextEntry().equals(EntryName.NULLBODY)) {
			return StateReturnValue.createRelevantResult(icapMessageDecoder.message);
		}
		return StateReturnValue.createIrrelevantResult();
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, StateEnum previousState) throws Exception {
		Encapsulated encapsulated = icapMessageDecoder.message.getEncapsulatedHeader();
		EntryName entry = encapsulated.getNextEntry();
		if(entry != null) {
			if(entry.equals(EntryName.RESHDR)) {
				return StateEnum.READ_HTTP_RESPONSE_INITIAL_AND_HEADERS;
			}
			if(entry.equals(EntryName.REQBODY) | entry.equals(EntryName.RESBODY)) {
				return StateEnum.BODY_PROCESSING_DECISION_STATE;
			}
		}
		return null;
	}

}
