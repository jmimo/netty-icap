package ch.mimo.netty.handler.codec.icap;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import ch.mimo.netty.handler.codec.icap.Encapsulated.EntryName;

public class ReadHttpResponseInitalAndHeadersState extends State<Object> {

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
		String line = IcapDecoderUtil.readLine(buffer,icapMessageDecoder.maxInitialLineLength);
		String[] initialLine = IcapDecoderUtil.splitInitialResponseLine(line);
		HttpResponse message = new DefaultHttpResponse(HttpVersion.valueOf(initialLine[0]),HttpResponseStatus.valueOf(Integer.parseInt(initialLine[1])));
		icapMessageDecoder.message.setHttpResponse(message);
		List<String[]> headerList = IcapDecoderUtil.readHeaders(buffer,icapMessageDecoder.maxHttpHeaderSize);
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
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws Exception {
		Encapsulated encapsulated = icapMessageDecoder.message.getEncapsulatedHeader();
		EntryName entry = encapsulated.getNextEntry();
		if(entry != null) {
			if(entry.equals(EntryName.REQHDR)) {
				return StateEnum.READ_HTTP_REQUEST_INITIAL_AND_HEADERS;
			}
			if(entry.equals(EntryName.REQBODY) | entry.equals(EntryName.RESBODY)) {
				return StateEnum.BODY_PROCESSING_DECISION_STATE;
			}
		}
		return null;
	}

}
