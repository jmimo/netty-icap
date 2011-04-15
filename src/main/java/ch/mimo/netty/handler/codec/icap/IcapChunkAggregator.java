package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

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

	private IcapMessage message;
	
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	Object msg = e.getMessage();
    	if(msg instanceof IcapMessage) {
    		IcapMessage currentMessage = (IcapMessage)msg;
    		message = currentMessage;
    	} else if(msg instanceof IcapChunk) {
    		IcapChunk chunk = (IcapChunk)msg;
    		if(chunk.isLast()) {
    			// TODO handle trailing headers
    			Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    			message = null;
    		} else {
	    		ChannelBuffer chunkBuffer = chunk.getContent();
	    		// TODO consider max length of content.
	    		if(message == null) {
	    			ctx.sendUpstream(e);
	    		} else if(message.getBody() == null || message.getBody().equals(IcapMessageElementEnum.NULLBODY)) {
	    			Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
	    		} else {
	    			if(message.getBody().equals(IcapMessageElementEnum.REQBODY)) {
	    				if(message.getHttpRequest() != null) {
	    					if(message.getHttpRequest().getContent().readableBytes() <= 0) {
	    						message.getHttpRequest().setContent(ChannelBuffers.dynamicBuffer());
	    					}
	    					message.getHttpRequest().getContent().writeBytes(chunkBuffer);
	    				} else {
	    					// no http request found but body marker indicates that the body is of this type.
	    				}
	    			} else if(message.getBody().equals(IcapMessageElementEnum.RESBODY)) {
	    				if(message.getHttpResponse() != null) {
	    					if(message.getHttpResponse().getContent().readableBytes() <= 0) {
	    						message.getHttpResponse().setContent(ChannelBuffers.dynamicBuffer());
	    					}
	    					message.getHttpResponse().getContent().writeBytes(chunkBuffer);
	    				} else {
	    					// no http response found but body marker indicates that the body is of this type.
	    				}
	    			} else {
	    				// invalid body marker found.
	    			}
	    		}
    		}
    	} else {
    		ctx.sendUpstream(e);
    	}
    }
}
