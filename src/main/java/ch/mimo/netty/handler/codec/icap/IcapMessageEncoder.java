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
import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public abstract class IcapMessageEncoder extends OneToOneEncoder {

	EncoderEmbedder<HttpRequestEncoder> httpRequestEncoderEmbedder;
	EncoderEmbedder<HttpResponseEncoder> httpResponseEncoderEmbedder;
	
    protected IcapMessageEncoder() {
        super();
        httpRequestEncoderEmbedder = new EncoderEmbedder<HttpRequestEncoder>(new HttpRequestEncoder());
        httpResponseEncoderEmbedder = new EncoderEmbedder<HttpResponseEncoder>(new HttpResponseEncoder());
    }
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if(msg instanceof IcapMessage) {
			IcapMessage message = (IcapMessage)msg;
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
			encodeInitialLine(buffer,message);
			encodeHeaders(buffer,message);
            buffer.writeByte(IcapCodecUtil.CR);
            buffer.writeByte(IcapCodecUtil.LF);
            int httpRequestSize = encodeHttpRequestHeader(buffer,message.getHttpRequest());
            int httpResponseSize = encodeHttpResponseHeader(buffer,message.getHttpResponse());
//            Encapsulated
            
            
			// TODO create Encapsulated header !
            // TODO remove chunked or content length details from http request and response before encoding.
            // TODO measure positions and write encapsulation header.
            // TODO wrap buffers and return request or response.
            // TODO how do I know the size of the preview! maybe a ChannelBuffer containing the preview would help here.
		} else if(msg instanceof IcapChunk) {
			// TODO implement chunk encoding
		}
		return null;
	}

	protected abstract int encodeInitialLine(ChannelBuffer buffer, IcapMessage message)  throws Exception;
	
	private int encodeHttpRequestHeader(ChannelBuffer buffer, HttpRequest httpRequest) throws UnsupportedEncodingException {
		if(httpRequest != null) {
			// TODO replace ASCII literal
			buffer.writeBytes(httpRequest.getMethod().toString().getBytes("ASCII"));
			buffer.writeByte(IcapCodecUtil.SP);
			buffer.writeBytes(httpRequest.getUri().getBytes("ASCII"));
			buffer.writeByte(IcapCodecUtil.SP);
			buffer.writeBytes(httpRequest.getProtocolVersion().toString().getBytes("ASCII"));
			buffer.writeByte(IcapCodecUtil.CR);
			buffer.writeByte(IcapCodecUtil.LF);
	        try {
	            for (Map.Entry<String, String> h: httpRequest.getHeaders()) {
	                encodeHeader(buffer, h.getKey(), h.getValue());
	            }
	        } catch (UnsupportedEncodingException e) {
	            throw (Error) new Error().initCause(e);
	        }
	        return buffer.readableBytes();
		}
		return -1;
	}
	
	private int encodeHttpResponseHeader(ChannelBuffer buffer, HttpResponse httpResponse) throws UnsupportedEncodingException {
		if(httpResponse != null) {
			// TODO replace ASCII literal
			buffer.writeBytes(httpResponse.getProtocolVersion().toString().getBytes("ASCII"));
			buffer.writeByte(IcapCodecUtil.SP);
			buffer.writeBytes(httpResponse.getStatus().toString().getBytes("ASCII"));
			buffer.writeByte(IcapCodecUtil.CR);
			buffer.writeByte(IcapCodecUtil.LF);
	        try {
	            for (Map.Entry<String, String> h: httpResponse.getHeaders()) {
	                encodeHeader(buffer, h.getKey(), h.getValue());
	            }
	        } catch (UnsupportedEncodingException e) {
	            throw (Error) new Error().initCause(e);
	        }
	        return buffer.readableBytes();
		}
		return -1;
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
    	// TODO replace ASCII literal
		buf.writeBytes(header.getBytes("ASCII"));
		buf.writeByte(IcapCodecUtil.COLON);
		buf.writeByte(IcapCodecUtil.SP);
		buf.writeBytes(value.getBytes("ASCII"));
		buf.writeByte(IcapCodecUtil.CR);
		buf.writeByte(IcapCodecUtil.LF);
    }
}
