package ch.mimo.netty.handler.codec.icap;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
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
    private Encapsulated.EntryName previousEncapsulationEntryName;
    
	private IcapMessage message;
	
	protected static enum State {
		SKIP_CONTROL_CHARS,
		READ_ICAP_INITIAL,
		READ_ICAP_HEADER,
		READ_MESSAGES
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
				IcapDecoderUtil.skipControlCharacters(buffer);
				checkpoint(State.READ_ICAP_INITIAL);
			}
			case READ_ICAP_INITIAL: {
				String[] initialLine = IcapDecoderUtil.splitInitialLine(IcapDecoderUtil.readLine(buffer,maxInitialLineLength));
				if(initialLine != null && initialLine.length == 3) {
					message = createMessage(initialLine);
					checkpoint(State.READ_ICAP_HEADER);
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
				encapsulated.setBufferOffsetIndex(buffer.readerIndex());
				message.setEncapsulatedHeader(encapsulated);
				checkpoint(State.READ_MESSAGES);
			}
			case READ_MESSAGES: {
				Encapsulated encapsulated = message.getEncapsulatedHeader();
				Encapsulated.EntryName entryName = encapsulated.getNextEntity(previousEncapsulationEntryName);
				while(entryName != null) {
					readEntity(entryName,buffer);
					entryCheckpoint(entryName);
					entryName = encapsulated.getNextEntity(previousEncapsulationEntryName);
				}
				return message;
			}
			default: {
				throw new Error("Shouldn't reach here.");
			}
		}
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
	
	private void entryCheckpoint(Encapsulated.EntryName entity) {
		previousEncapsulationEntryName = entity;
		checkpoint(State.READ_MESSAGES);
	}
	
	private void readEntity(Encapsulated.EntryName entity, ChannelBuffer buffer) throws TooLongFrameException {
		if(entity.equals(Encapsulated.EntryName.REQHDR)) {
			String[] initialLine = IcapDecoderUtil.splitInitialLine(IcapDecoderUtil.readLine(buffer,maxInitialLineLength));
			HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.valueOf(initialLine[2]),HttpMethod.valueOf(initialLine[0]),initialLine[1]);
			message.setHttpRequest(httpRequest);
			List<String[]> headerList = readHeaders(buffer,maxHttpHeaderSize);
			message.getHttpRequest().clearHeaders();
			for(String[] header : headerList) {
				message.getHttpRequest().addHeader(header[0],header[1]);
			}
		} else if(entity.equals(Encapsulated.EntryName.RESHDR)) {
			String[] initialLine = IcapDecoderUtil.splitInitialResponseLine(IcapDecoderUtil.readLine(buffer,maxInitialLineLength));
			HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.valueOf(initialLine[0]),HttpResponseStatus.valueOf(Integer.valueOf(initialLine[1])));
			message.setHttpResponse(httpResponse);
			List<String[]> headerList = readHeaders(buffer,maxHttpHeaderSize);
			message.getHttpResponse().clearHeaders();
			for(String[] header : headerList) {
				message.getHttpResponse().addHeader(header[0],header[1]);
			}
		} else if(entity.equals(Encapsulated.EntryName.REQBODY)) {
			
		} else if(entity.equals(Encapsulated.EntryName.RESBODY)) {
			
		} else if(entity.equals(Encapsulated.EntryName.NULLBODY)) {
			// no body in message (special tag)
		} else {
			// TODO: should not reach here!
		}
	}
}
