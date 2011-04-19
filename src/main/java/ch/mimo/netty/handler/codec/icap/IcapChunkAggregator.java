package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;

/**
 * This Icap chunk aggregator will receive the icap message and store the body
 * If it exists into the respective http request or response that is transported
 * with this icap message.
 * 
 * If the received icap message is a preview no action is taken!
 * QUESTION: we could allow that a preview message is collected and kept.
 * Once the 100 continue is sent the message is completed.
 * 
 * @author Michael Mimo Moratti
 *
 */
public class IcapChunkAggregator extends SimpleChannelUpstreamHandler {

	private long maxContentLength;
	private IcapMessage message;
	
	public IcapChunkAggregator(long maxContentLength) {
		this.maxContentLength = maxContentLength;
	}
	
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	Object msg = e.getMessage();
    	if(msg instanceof IcapMessage) {
    		IcapMessage currentMessage = (IcapMessage)msg;
    		message = currentMessage;
    		if(message.getBody() == null || message.getBody().equals(IcapMessageElementEnum.NULLBODY)) {
    			Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    			message = null;
    			return;
    		}
    	} else if(msg instanceof IcapChunkTrailer) {
    		if(message == null) {
    			ctx.sendUpstream(e);
    		} else {
    			IcapChunkTrailer trailer = (IcapChunkTrailer)msg;
    			if(trailer.getHeaderNames().size() > 0) {		
    				for(String name : trailer.getHeaderNames()) {
    					if(message.getBody().equals(IcapMessageElementEnum.REQBODY)) {
    						message.getHttpRequest().addHeader(name,trailer.getHeader(name));
    					} else if(message.getBody().equals(IcapMessageElementEnum.RESBODY)) {
    						message.getHttpResponse().addHeader(name,trailer.getHeader(name));
    					}	
    				}
    			}
    			Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    		}
    	} else if(msg instanceof IcapChunk) {
    		IcapChunk chunk = (IcapChunk)msg;
    		if(message == null) {
    			ctx.sendUpstream(e);
    		} else if(chunk.isLast()) {
    			Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    			message = null;
    		} else {
	    		ChannelBuffer chunkBuffer = chunk.getContent();
	    		ChannelBuffer content = null;
    			if(message.getBody().equals(IcapMessageElementEnum.REQBODY)) {
    				if(message.getHttpRequest() != null) {
    					if(message.getHttpRequest().getContent().readableBytes() <= 0) {
    						message.getHttpRequest().setContent(ChannelBuffers.dynamicBuffer());
    					}
    					content = message.getHttpRequest().getContent();
    				} else {
    					throw new IcapDecodingError("unable attaching chunked content to http request, beaause it does not exist");
    				}
    			} else if(message.getBody().equals(IcapMessageElementEnum.RESBODY)) {
    				if(message.getHttpResponse() != null) {
    					if(message.getHttpResponse().getContent().readableBytes() <= 0) {
    						message.getHttpResponse().setContent(ChannelBuffers.dynamicBuffer());
    					}
    					content = message.getHttpResponse().getContent();
    				} else {
    					throw new IcapDecodingError("unable attaching chunked content to http response, beaause it does not exist");
    				}
    			} else {
    				throw new IcapDecodingError("Invalid encapsulation value found [" + message.getBody().name() + "]");
    			}
    			if(content.readableBytes() > maxContentLength - chunkBuffer.readableBytes()) {
    				throw new TooLongFrameException("ICAP content length exceeded [" + maxContentLength + "] bytes");
    			} else {
    				content.writeBytes(chunkBuffer);
    			}
    		}
    	} else {
    		ctx.sendUpstream(e);
    	}
    }
}
