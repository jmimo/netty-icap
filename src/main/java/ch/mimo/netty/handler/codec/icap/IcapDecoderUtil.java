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
	
	/**
	 * reads one individual header "key: value"
	 * @param buffer which contains the request stream
	 * @param headerSize the current header size, accumulated for all headers.
	 * @param maxHeaderSize the maximal allowed header length (all headers combined).
	 * @param headerType error message prefix like "ICAP" or "HTTP Request"
	 * @return one complete header containing key and value.
	 * @throws TooLongFrameException In case the total header length is exceeded.
	 */
	public static String readHeader(ChannelBuffer buffer, int headerSize, int maxHeaderSize, String headerType) throws TooLongFrameException {
		StringBuilder sb = new StringBuilder(64);
		loop: for (;;) {
			char nextByte = (char) buffer.readByte();
			headerSize++;

			switch (nextByte) {
			case IcapCodecUtil.CR:
				nextByte = (char) buffer.readByte();
				headerSize++;
				if (nextByte == IcapCodecUtil.LF) {
					break loop;
				}
				break;
			case IcapCodecUtil.LF:
				break loop;
			}

			// Abort decoding if the header part is too large.
			if (headerSize >= maxHeaderSize) {
				throw new TooLongFrameException(headerType + " header is larger than " + maxHeaderSize + " bytes.");
			}

			sb.append(nextByte);
		}
		return sb.toString();
	}
}
