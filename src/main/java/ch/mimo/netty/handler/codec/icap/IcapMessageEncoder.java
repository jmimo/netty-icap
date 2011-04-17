/*******************************************************************************
 * Copyright (c) 2011 Michael Mimo Moratti.
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

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public abstract class IcapMessageEncoder extends OneToOneEncoder {
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if(msg instanceof IcapMessage) {
			IcapMessage message = (IcapMessage)msg;
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
			encodeInitialLine(buffer,message);
			encodeHeaders(buffer,message);
			ChannelBuffer httpRequestBuffer = encodeHttpRequestHeader(message.getHttpRequest());
			ChannelBuffer httpResponseBuffer = encodeHttpResponseHeader(message.getHttpResponse());
            int index = 0;
            Encapsulated encapsulated = new Encapsulated();
            if(httpRequestBuffer.readableBytes() > 0) {
            	encapsulated.addEntry(IcapMessageElementEnum.REQHDR,index);
            	httpRequestBuffer.writeBytes(IcapCodecUtil.CRLF);
            	index += httpRequestBuffer.readableBytes();
            }
            if(httpResponseBuffer.readableBytes() > 0) {
            	encapsulated.addEntry(IcapMessageElementEnum.RESHDR,index);
            	httpResponseBuffer.writeBytes(IcapCodecUtil.CRLF);
            	index += httpResponseBuffer.readableBytes();
            }
            if(message.getBody() != null) {
            	encapsulated.addEntry(message.getBody(),index);
            } else {
            	encapsulated.addEntry(IcapMessageElementEnum.NULLBODY,index);
            }
            encapsulated.encode(buffer);
            buffer.writeBytes(httpRequestBuffer);
            buffer.writeBytes(httpResponseBuffer);
            return buffer;
		} else if(msg instanceof IcapChunk) {
			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
			IcapChunk chunk = (IcapChunk)msg;
			if(chunk.isLast()) {
				if(chunk.isEarlyTerminated()) {
					buffer.writeBytes(IcapCodecUtil.NATIVE_IEOF_SEQUENCE);
					buffer.writeBytes(IcapCodecUtil.CRLF);
					buffer.writeBytes(IcapCodecUtil.CRLF);
				} else if(msg instanceof IcapChunkTrailer) { 
					buffer.writeByte((byte) '0');
					buffer.writeBytes(IcapCodecUtil.CRLF);
					encodeTrailingHeaders(buffer,(IcapChunkTrailer)msg);
					buffer.writeBytes(IcapCodecUtil.CRLF);
				} else {
					buffer.writeByte((byte) '0');
					buffer.writeBytes(IcapCodecUtil.CRLF);
					buffer.writeBytes(IcapCodecUtil.CRLF);
//					buffer.writeBytes(IcapCodecUtil.CRLF);
				}
			} else {
				ChannelBuffer chunkBuffer = chunk.getContent();
				int contentLength = chunkBuffer.readableBytes();
				buffer.writeBytes(Integer.toHexString(contentLength).getBytes(IcapCodecUtil.ASCII_CHARSET));
				buffer.writeBytes(IcapCodecUtil.CRLF);
				buffer.writeBytes(chunkBuffer);
				buffer.writeBytes(IcapCodecUtil.CRLF);
			}
			return buffer;
		}
		return null;
	}

	protected abstract int encodeInitialLine(ChannelBuffer buffer, IcapMessage message)  throws Exception;
	
	private ChannelBuffer encodeHttpRequestHeader(HttpRequest httpRequest) throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		if(httpRequest != null) {
			buffer.writeBytes(httpRequest.getMethod().toString().getBytes(IcapCodecUtil.ASCII_CHARSET));
			buffer.writeByte(IcapCodecUtil.SP);
			buffer.writeBytes(httpRequest.getUri().getBytes(IcapCodecUtil.ASCII_CHARSET));
			buffer.writeByte(IcapCodecUtil.SP);
			buffer.writeBytes(httpRequest.getProtocolVersion().toString().getBytes(IcapCodecUtil.ASCII_CHARSET));
			buffer.writeBytes(IcapCodecUtil.CRLF);
	        try {
	            for (Map.Entry<String, String> h: httpRequest.getHeaders()) {
	                encodeHeader(buffer, h.getKey(), h.getValue());
	            }
	        } catch (UnsupportedEncodingException e) {
	            throw (Error) new Error().initCause(e);
	        }
		}
		return buffer;
	}
	
	private ChannelBuffer encodeHttpResponseHeader(HttpResponse httpResponse) throws UnsupportedEncodingException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		if(httpResponse != null) {
			buffer.writeBytes(httpResponse.getProtocolVersion().toString().getBytes(IcapCodecUtil.ASCII_CHARSET));
			buffer.writeByte(IcapCodecUtil.SP);
			buffer.writeBytes(httpResponse.getStatus().toString().getBytes(IcapCodecUtil.ASCII_CHARSET));
			buffer.writeBytes(IcapCodecUtil.CRLF);
	        try {
	            for (Map.Entry<String, String> h: httpResponse.getHeaders()) {
	                encodeHeader(buffer, h.getKey(), h.getValue());
	            }
	        } catch (UnsupportedEncodingException e) {
	            throw (Error) new Error().initCause(e);
	        }
		}
		return buffer;
	}
	
    private int encodeTrailingHeaders(ChannelBuffer buffer, IcapChunkTrailer chunkTrailer) {
    	int index = buffer.readableBytes();
        try {
            for (Map.Entry<String, String> h: chunkTrailer.getHeaders()) {
                encodeHeader(buffer, h.getKey(), h.getValue());
            }
        } catch (UnsupportedEncodingException e) {
            throw (Error) new Error().initCause(e);
        }
        return buffer.readableBytes() - index;
    }
	
    private int encodeHeaders(ChannelBuffer buffer, IcapMessage message) {
    	int index = buffer.readableBytes();
        try {
            for (Map.Entry<String, String> h: message.getHeaders()) {
                encodeHeader(buffer, h.getKey(), h.getValue());
            }
        } catch (UnsupportedEncodingException e) {
            throw (Error) new Error().initCause(e);
        }
        return buffer.readableBytes() - index;
    }
    
    // TODO return the length
    private void encodeHeader(ChannelBuffer buf, String header, String value) throws UnsupportedEncodingException {
		buf.writeBytes(header.getBytes(IcapCodecUtil.ASCII_CHARSET));
		buf.writeByte(IcapCodecUtil.COLON);
		buf.writeByte(IcapCodecUtil.SP);
		buf.writeBytes(value.getBytes(IcapCodecUtil.ASCII_CHARSET));
		buf.writeBytes(IcapCodecUtil.CRLF);
    }
}
