package ch.mimo.netty.handler.codec.icap;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

public abstract class IcapMessageDecoder extends ReplayingDecoder<IcapMessageDecoder.State> {

    private final int maxInitialLineLength;
    private final int maxIcapHeaderSize;
    private final int maxHttpHeaderSize;
    private final int maxChunkSize;
    
    private int encapsulationOffset;
    
	private IcapMessage message;
	
	protected static enum State {
		SKIP_CONTROL_CHARS,
		READ_ICAP_INITIAL,
		READ_ICAP_HEADER,
		READ_ICAP_OPTIONS_REQUEST,
		READ_HTTP_REQUEST_INITIAL,
		READ_HTTP_REQUEST_HEADER,
		READ_HTTP_RESPONSE_INITIAL,
		READ_HTTP_RESPONSE_HEADER,
		READ_HTTP_BODY,
		END_OF_REQUEST
	}
	
    /**
     * Creates a new instance with the default
     * {@code maxInitialLineLength (4096}}, {@code maxIcapHeaderSize (8192)}, {@code maxHttpHeaderSize (8192)}, and
     * {@code maxChunkSize (8192)}.
     */
    protected IcapMessageDecoder() {
        this(4096,8192,8192,8192);
        encapsulationOffset = 0;
    }
    
    /**
     * Creates a new instance with the specified parameters.
     */
    protected IcapMessageDecoder(int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
        super(State.SKIP_CONTROL_CHARS,true);
        if (maxInitialLineLength <= 0) {
            throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + maxInitialLineLength);
        }
        if (maxIcapHeaderSize <= 0) {
            throw new IllegalArgumentException("maxIcapHeaderSize must be a positive integer: " + maxIcapHeaderSize);
        }
        if (maxChunkSize < 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
        }
        this.maxInitialLineLength = maxInitialLineLength;
        this.maxIcapHeaderSize = maxIcapHeaderSize;
        this.maxHttpHeaderSize = maxHttpHeaderSize;
        this.maxChunkSize = maxChunkSize;
    }

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, State state) throws Exception {
		switch (state) {
			case SKIP_CONTROL_CHARS: {
	            try {
	                IcapDecoderUtil.skipControlCharacters(buffer);
	                checkpoint(State.READ_ICAP_INITIAL);
	            } finally {
	                checkpoint();
	            }
			}
			case READ_ICAP_INITIAL: {
				String[] initialLine = IcapDecoderUtil.splitInitialLine(IcapDecoderUtil.readLine(buffer,maxInitialLineLength));
				if(initialLine != null && initialLine.length == 3) {
					message = createMessage(initialLine);
				} else {
					checkpoint(State.SKIP_CONTROL_CHARS);
					return null;
				}
			}
			case READ_ICAP_HEADER: {
				List<String[]> headerList = readHeaders(buffer,maxIcapHeaderSize);
				message.clearHeaders();
				for(String[] header : headerList) {
					message.addHeader(header[0],header[1]);
				}
				// validate mandatory icap headers
				if(!message.containsHeader(IcapHeaders.Names.HOST)) {
					throw new Error("Mandatory ICAP message header [Host] is missing");
				}
				if(!message.containsHeader(IcapHeaders.Names.ENCAPSULATED)) {
					throw new Error("Mandatory ICAP message header [Encapsulated] is missing");
				}
				Encapsulated encapsulated = Encapsulated.parseHeader(message.getHeader(IcapHeaders.Names.ENCAPSULATED));
				message.setEncapsulatedHeader(encapsulated);
				// make first parsing direction decision
				// a. if OPTIONS
				// b. if REQ / RESP mod
				if(message.getMethod().equals(IcapMethod.OPTIONS)) {
					checkpoint(State.READ_ICAP_OPTIONS_REQUEST);
				} else {
					if(encapsulated.getPosition(Encapsulated.REQHDR) == 0) {
						checkpoint(State.READ_HTTP_REQUEST_INITIAL);
					} else {
						// NOOP this is not what I expected. If the method is not OPTIONS and REQHDR is not null the request does not
						// comply with the RFC. When we reply we can parse this section again... 
					}
				}
			}
			case READ_HTTP_REQUEST_INITIAL: {
				encapsulationOffset = buffer.readerIndex();
				String[] initialLine = IcapDecoderUtil.splitInitialLine(IcapDecoderUtil.readLine(buffer,maxInitialLineLength));
				HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.valueOf(initialLine[2]),HttpMethod.valueOf(initialLine[0]),initialLine[1]);
				this.message.setHttpRequest(httpRequest);
				checkpoint(State.READ_HTTP_REQUEST_HEADER);
			}
			case READ_HTTP_REQUEST_HEADER: {
				List<String[]> headerList = readHeaders(buffer,maxHttpHeaderSize);
				for(String[] header : headerList) {
					message.getHttpRequest().addHeader(header[0],header[1]);
				}
				// TODO validate buffer index with encapsulation value, don't know whether this check is necessary?
				if(message.getEncapsulatedHeader().getPosition(Encapsulated.RESHDR) > -1) {
					checkpoint(State.READ_HTTP_RESPONSE_INITIAL);
				} else if(message.getEncapsulatedHeader().getPosition(Encapsulated.RESBODY) > -1) {
					// TODO handle null- & opt-body
					checkpoint(State.READ_HTTP_BODY);
				} else {
					checkpoint(State.END_OF_REQUEST);
				}
			}
			case READ_HTTP_RESPONSE_INITIAL: {
				// TODO offset the buffer first.
				String[] initialLine = IcapDecoderUtil.splitInitialResponseLine(IcapDecoderUtil.readLine(buffer,maxInitialLineLength));
				HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.valueOf(initialLine[0]),HttpResponseStatus.valueOf(Integer.valueOf(initialLine[1])));
				this.message.setHttpResponse(httpResponse);
				checkpoint(State.READ_HTTP_RESPONSE_HEADER);
			}
			case READ_HTTP_RESPONSE_HEADER: {
				List<String[]> headerList = readHeaders(buffer,maxHttpHeaderSize);
				for(String[] header : headerList) {
					message.getHttpResponse().addHeader(header[0],header[1]);
				}
				if(message.getEncapsulatedHeader().getPosition(Encapsulated.RESBODY) > -1) {
					// TODO handle null- & opt-body
					checkpoint(State.READ_HTTP_BODY);
				} else {
					checkpoint(State.END_OF_REQUEST);
				}
			}
			case READ_HTTP_BODY: {
				// TODO handle Preview and chunks!
			}
			case END_OF_REQUEST: {
				// NOOP
			}
			default: {
				throw new Error("Shouldn't reach here.");
			}
		}
		return message;
	}
	
	public abstract boolean isDecodingRequest();
	
	protected abstract IcapMessage createMessage(String[] initialLine);
	
	private List<String[]> readHeaders(ChannelBuffer buffer, int maxSize) throws TooLongFrameException {
		List<String[]> headerList = new ArrayList<String[]>();
		SizeDelimiter sizeDelimiter = new SizeDelimiter(maxIcapHeaderSize);
		String line = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
		String name = null;
		String value = null;
		if(line.length() != 0) {
			while(line.length() != 0) {
				if(name != null && IcapDecoderUtil.isHeaderLineSimpleValue(line)) {
					value = value + ' ' + line.trim();
				} else {
					if(name != null) {
						headerList.add(new String[]{name,value});
					}
					String[] header = IcapDecoderUtil.splitHeader(line);
					name = header[0];
					value = header[1];
				}
				line = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
			}
            if (name != null) {
            	headerList.add(new String[]{name,value});
            }
		}
		return headerList;
	}
}
