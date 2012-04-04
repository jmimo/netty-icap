/*******************************************************************************
 * Copyright 2011 - 2012 Michael Mimo Moratti
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ch.mimo.netty.handler.codec.icap;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpChunkTrailer;
import org.jboss.netty.handler.codec.http.HttpHeaders;

/**
 * Decoder State that reads http trailing headers.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see StateEnum
 */
public class ReadTrailingHeadersState extends State<Object> {

	public ReadTrailingHeadersState(String name) {
		super(name);
	}
	
	@Override
	public void onEntry(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
	}

	@Override
	public StateReturnValue execute(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder) throws DecodingException {
		SizeDelimiter sizeDelimiter = new SizeDelimiter(icapMessageDecoder.maxHttpHeaderSize);
		boolean preview = icapMessageDecoder.message.isPreviewMessage();
        String line = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
        String lastHeader = null;
        if (line.length() != 0) {
            HttpChunkTrailer trailer = new DefaultIcapChunkTrailer(preview,false);
            do {
                char firstChar = line.charAt(0);
                if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
                    List<String> current = trailer.getHeaders(lastHeader);
                    if (current.size() != 0) {
                        int lastPos = current.size() - 1;
                        String newString = current.get(lastPos) + line.trim();
                        current.set(lastPos, newString);
                    } else {
                        // Content-Length, Transfer-Encoding, or Trailer
                    }
                } else {
                    String[] header = IcapDecoderUtil.splitHeader(line);
                    String name = header[0];
                    if (!name.equalsIgnoreCase(HttpHeaders.Names.CONTENT_LENGTH) &&
                        !name.equalsIgnoreCase(HttpHeaders.Names.TRANSFER_ENCODING) &&
                        !name.equalsIgnoreCase(HttpHeaders.Names.TRAILER)) {
                        trailer.addHeader(name, header[1]);
                    }
                    lastHeader = name;
                }

                line = IcapDecoderUtil.readSingleHeaderLine(buffer,sizeDelimiter);
            } while (line.length() != 0);

            return StateReturnValue.createRelevantResult(trailer);
        }
        return StateReturnValue.createRelevantResult(new DefaultIcapChunkTrailer(preview,false));
	}

	@Override
	public StateEnum onExit(ChannelBuffer buffer, IcapMessageDecoder icapMessageDecoder, Object decisionInformation) throws DecodingException {
		return null;
	}
}
