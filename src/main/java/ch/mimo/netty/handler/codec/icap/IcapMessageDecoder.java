package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

public abstract class IcapMessageDecoder extends ReplayingDecoder<IcapMessageDecoder.State> {

    private final int maxInitialLineLength;
    private final int maxIcapHeaderSize;
    private final int maxChunkSize;
	private IcapMessage message;
	
	protected static enum State {
		SKIP_CONTROL_CHARS,
		READ_ICAP_INITIAL,
		READ_ICAP_HEADER
	}
	
    /**
     * Creates a new instance with the default
     * {@code maxInitialLineLength (4096}}, {@code maxIcapHeaderSize (8192)}, and
     * {@code maxChunkSize (8192)}.
     */
    protected IcapMessageDecoder() {
        this(4096, 8192, 8192);
    }
    
    /**
     * Creates a new instance with the specified parameters.
     */
    protected IcapMessageDecoder(int maxInitialLineLength, int maxIcapHeaderSize, int maxChunkSize) {
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
            if (initialLine.length < 3) {
                // Invalid initial line - ignore.
                checkpoint(State.SKIP_CONTROL_CHARS);
                return null;
            }
            
            message = createMessage(initialLine);
            checkpoint(State.READ_ICAP_HEADER);
		}
		case READ_ICAP_HEADER: {
			State nextState = readIcapHeaders(buffer);
		}
		default:
			break;
		}
		
		
		// TODO parse and store icap headers with the HttpHeaders class.
		// The IcapHeaders class will provide the missing headers "Preview" and "Encapsulation"
		// plus the parsing functionality for the Encapsulation header!
		return message;
	}
	
	public abstract boolean isDecodingRequest();
	
	protected abstract IcapMessage createMessage(String[] initialLine) throws Exception;
	
	private State readIcapHeaders(ChannelBuffer buffer) throws TooLongFrameException {
		SizeDelimiter sizeDelimiter = new SizeDelimiter(maxIcapHeaderSize);
		String line = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
		String name = null;
		String value = null;
		if(line.length() != 0) {
			message.clearHeaders();
			while(line.length() != 0) {
				if(name != null && IcapDecoderUtil.isHeaderLineSimpleValue(line)) {
					value = value + ' ' + line.trim();
				} else {
					if(name != null) {
						message.addHeader(name,value);
					}
					String[] header = IcapDecoderUtil.splitHeader(line);
					name = header[0];
					value = header[1];
				}
				line = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
			}
            if (name != null) {
                message.addHeader(name,value);
            }
		}
		// validate icap headers
		if(message.containsHeader(IcapHeaders.Names.HOST)) {
			throw new Error("Mandatory ICAP message header [Host] is missing");
		}
		if(message.containsHeader(IcapHeaders.Names.ENCAPSULATED)) {
			throw new Error("Mandatory ICAP message header [Encapsulated] is missing");
		}
		// TODO parse encapsulation header for subsequent parsing
		return State.SKIP_CONTROL_CHARS;
	}
	
	private void parseEncapsulatedInformation() {
		String contents = message.getHeader(IcapHeaders.Names.ENCAPSULATED);
		
	}
}
