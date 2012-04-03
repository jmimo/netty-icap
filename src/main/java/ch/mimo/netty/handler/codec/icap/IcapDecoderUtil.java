/*******************************************************************************
 * Copyright (c) 2012 Michael Mimo Moratti.
 *
 * Michael Mimo Moratti licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package ch.mimo.netty.handler.codec.icap;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;

/**
 * Utility that provides decoding support for ICAP messages.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public final class IcapDecoderUtil {

	private IcapDecoderUtil() {
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
	
    /**
     * reads a line until CR / LF / CRLF
     * @param buffer
     * @param maxLineLength
     * @return the first line found in the buffer.
     * @throws TooLongFrameException
     */
    public static String readLine(ChannelBuffer buffer, int maxLineLength) throws DecodingException {
        StringBuilder sb = new StringBuilder(64);
        int lineLength = 0;
        while (true) {
            byte nextByte = buffer.readByte();
            if (nextByte == IcapCodecUtil.CR) {
                nextByte = buffer.readByte();
                if (nextByte == IcapCodecUtil.LF) {
                    return sb.toString();
                }
            } else if (nextByte == IcapCodecUtil.LF) {
                return sb.toString();
            } else {
                if (lineLength >= maxLineLength) {
                    throw new DecodingException(new TooLongFrameException(
                            "An HTTP line is larger than " + maxLineLength +
                            " bytes."));
                }
                lineLength ++;
                sb.append((char) nextByte);
            }
        }
    }
    
    /**
     * previews a line until CR / LF / CRLF
     * this will not increase the buffers readerIndex!
     * @param buffer
     * @param maxLineLength
     * @return the first line found in the buffer
     * @throws DecodingException
     */
    public static String previewLine(ChannelBuffer buffer, int maxLineLength) throws DecodingException {
        StringBuilder sb = new StringBuilder(64);
        int lineLength = 0;
        for(int i = buffer.readerIndex() ; i < buffer.readableBytes() ; i++) {
            byte nextByte = buffer.getByte(i);
            if (nextByte == IcapCodecUtil.CR) {
                nextByte = buffer.getByte(++i);
                if (nextByte == IcapCodecUtil.LF) {
                	break;
                }
            } else if (nextByte == IcapCodecUtil.LF) {
            	break;
            } else {
                if (lineLength >= maxLineLength) {
                    throw new DecodingException(new TooLongFrameException(
                            "An HTTP line is larger than " + maxLineLength +
                            " bytes."));
                }
                lineLength ++;
                sb.append((char) nextByte);
            }
        }
        return sb.toString();
    }    
	
    /**
     * Splits an initial line.
     * @param sb
     * @return string array containing all elements found in the initial line.
     */
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

	/**
	 * finds the first occurrence of a non whitespace character.
	 * @param sb string to find non-whitespaces in
	 * @param offset the offset to start searching from.
	 * @return the position of the first non-whitespace
	 */
	public static int findNonWhitespace(String sb, int offset) {
		int result;
		for (result = offset; result < sb.length(); result++) {
			if (!Character.isWhitespace(sb.charAt(result))) {
				break;
			}
		}
		return result;
	}

	/**
	 * finds the first occurrence of a whitespace character
	 * @param sb string to find whitespaces in.
	 * @param offset to search from within the string.
	 * @return the position of the first whitespace.
	 */
	public static int findWhitespace(String sb, int offset) {
		int result;
		for (result = offset; result < sb.length(); result++) {
			if (Character.isWhitespace(sb.charAt(result))) {
				break;
			}
		}
		return result;
	}

	/**
	 * finds the end of a string.
	 * @param sb string to find the end from
	 * @return the end position.
	 */
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
	 * parses the chunk size from a line.
	 * 
	 * @param line
	 * @return -1 if the chunk size indicates that a preview message is early terminated.
	 */
    public static int getChunkSize(String line) throws DecodingException {
        String hex = line.trim();
        if(hex.equals(IcapCodecUtil.IEOF_SEQUENCE_STRING)) {
        	return -1;
        }
        for (int i = 0; i < hex.length(); i ++) {
            char c = hex.charAt(i);
            if (c == ';' || Character.isWhitespace(c) || Character.isISOControl(c)) {
                hex = hex.substring(0, i);
                break;
            }
        }
        try {
        	return Integer.parseInt(hex, 16);
        } catch(NumberFormatException nfe) {
        	throw new DecodingException(nfe);
        }
    }
	
    /**
     * parses all available message headers.
     * @param buffer @see {@link ChannelBuffer} that contains the headers.
     * @param maxSize the maximum size of all headers concatenated.
     * @return a list of String arrays containing [0] key [1] value of each header.
     * @throws TooLongFrameException if the maximum size is reached.
     */
	public static List<String[]> readHeaders(ChannelBuffer buffer, int maxSize) throws DecodingException {
		List<String[]> headerList = new ArrayList<String[]>();
		SizeDelimiter sizeDelimiter = new SizeDelimiter(maxSize);
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
	public static String readSingleHeaderLine(ChannelBuffer buffer, SizeDelimiter sizeDelimiter) throws DecodingException {
		StringBuilder sb = new StringBuilder(64);
		loop: for (;;) {
			char nextByte = (char) buffer.readByte();
			sizeDelimiter.increment();

			if(nextByte == IcapCodecUtil.CR) {
				nextByte = (char) buffer.readByte();
				sizeDelimiter.increment();
				if (nextByte == IcapCodecUtil.LF) {
					break loop;
				}
			} else if(nextByte == IcapCodecUtil.LF) {
				break loop;
			}
			sb.append(nextByte);
		}
		return sb.toString();
	}
	
	/**
	 * Splits one header into key|value
	 * @param sb
	 * @return string array containing the key [0] and value [1] separated.
	 */
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
