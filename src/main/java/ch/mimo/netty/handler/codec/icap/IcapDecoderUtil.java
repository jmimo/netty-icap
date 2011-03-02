package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;

public final class IcapDecoderUtil {

	private IcapDecoderUtil() {
		super();
	}

	/**
	 * finds the true beginning of the request 
	 * by skipping all prepended control and whitespace characters.
	 * @param buffer
	 */
    public static void skipControlCharacters(ChannelBuffer buffer) {
        for (;;) {
            char c = (char) buffer.readUnsignedByte();
            if (!Character.isISOControl(c) &&
                !Character.isWhitespace(c)) {
                buffer.readerIndex(buffer.readerIndex() - 1);
                break;
            }
        }
    }
	
    public static String readLine(ChannelBuffer buffer, int maxLineLength) throws TooLongFrameException {
        StringBuilder sb = new StringBuilder(64);
        int lineLength = 0;
        while (true) {
            byte nextByte = buffer.readByte();
            if (nextByte == IcapCodecUtil.CR) {
                nextByte = buffer.readByte();
                if (nextByte == IcapCodecUtil.LF) {
                    return sb.toString();
                }
            }
            else if (nextByte == IcapCodecUtil.LF) {
                return sb.toString();
            }
            else {
                if (lineLength >= maxLineLength) {
                    throw new TooLongFrameException(
                            "An HTTP line is larger than " + maxLineLength +
                            " bytes.");
                }
                lineLength ++;
                sb.append((char) nextByte);
            }
        }
    }
	
	public static String[] splitInitialLine(String sb) {
		int aStart;
		int aEnd;
		int bStart;
		int bEnd;
		int cStart;
		int cEnd;

		aStart = findNonWhitespace(sb, 0);
		aEnd = findWhitespace(sb, aStart);

		bStart = findNonWhitespace(sb, aEnd);
		bEnd = findWhitespace(sb, bStart);

		cStart = findNonWhitespace(sb, bEnd);
		cEnd = findEndOfString(sb);

		return new String[] { sb.substring(aStart, aEnd),
				sb.substring(bStart, bEnd),
				cStart < cEnd ? sb.substring(cStart, cEnd) : "" };
	}

	public static int findNonWhitespace(String sb, int offset) {
		int result;
		for (result = offset; result < sb.length(); result++) {
			if (!Character.isWhitespace(sb.charAt(result))) {
				break;
			}
		}
		return result;
	}

	public static int findWhitespace(String sb, int offset) {
		int result;
		for (result = offset; result < sb.length(); result++) {
			if (Character.isWhitespace(sb.charAt(result))) {
				break;
			}
		}
		return result;
	}

	public static int findEndOfString(String sb) {
		int result;
		for (result = sb.length(); result > 0; result--) {
			if (!Character.isWhitespace(sb.charAt(result - 1))) {
				break;
			}
		}
		return result;
	}
	
//	public static String readCompleteHeaderSection(ChannelBuffer buffer, SizeDelimiter sizeDelimiter) throws TooLongFrameException {
//		String line = readSingleHeaderLine(buffer,sizeDelimiter);
//		while(line.length() != 0) {
//			
//		}
//		return null;
//	}
	
	public static boolean isHeaderLineSimpleValue(String header) {
		char firstChar = header.charAt(0);
		return firstChar == ' ' || firstChar == '\t';
	}
	
	/**
	 * reads one individual header "key: value"
	 * @param buffer which contains the request stream
	 * @param sizeDelimiter the current header size, accumulated for all headers.
	 * @return one complete header containing key and value.
	 * @throws TooLongFrameException In case the total header length is exceeded.
	 */
	public static String readSingleHeaderLine(ChannelBuffer buffer, SizeDelimiter sizeDelimiter) throws TooLongFrameException {
		StringBuilder sb = new StringBuilder(64);
		loop: for (;;) {
			char nextByte = (char) buffer.readByte();
			sizeDelimiter.increment();

			switch (nextByte) {
			case IcapCodecUtil.CR:
				nextByte = (char) buffer.readByte();
				sizeDelimiter.increment();
				if (nextByte == IcapCodecUtil.LF) {
					break loop;
				}
				break;
			case IcapCodecUtil.LF:
				break loop;
			}
			sb.append(nextByte);
		}
		return sb.toString();
	}
	
    public static String[] splitHeader(String sb) {
        final int length = sb.length();
        int nameStart;
        int nameEnd;
        int colonEnd;
        int valueStart;
        int valueEnd;

        nameStart = findNonWhitespace(sb, 0);
        for (nameEnd = nameStart; nameEnd < length; nameEnd ++) {
            char ch = sb.charAt(nameEnd);
            if (ch == ':' || Character.isWhitespace(ch)) {
                break;
            }
        }

        for (colonEnd = nameEnd; colonEnd < length; colonEnd ++) {
            if (sb.charAt(colonEnd) == ':') {
                colonEnd ++;
                break;
            }
        }

        valueStart = findNonWhitespace(sb, colonEnd);
        if (valueStart == length) {
            return new String[] {
                    sb.substring(nameStart, nameEnd),
                    ""
            };
        }

        valueEnd = findEndOfString(sb);
        return new String[] {
                sb.substring(nameStart, nameEnd),
                sb.substring(valueStart, valueEnd)
        };
    }
}
