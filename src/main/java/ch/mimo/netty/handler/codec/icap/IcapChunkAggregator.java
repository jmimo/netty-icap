package ch.mimo.netty.handler.codec.icap;

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

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	Object msg = e.getMessage();
    	if(msg instanceof IcapMessage) {
    		IcapMessage message = (IcapMessage)msg;
    		// TODO validate the messages equality and if the message is a new one distroy the context.
    		if(message.getBody() == null | message.getBody().equals(IcapMessageElementEnum.NULLBODY)) {
    			Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    		} else if(message.isPreviewMessage()) {
    			Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    		} else {
    			// TODO add the to be received body into the respective http message
    		}
    	} else if(msg instanceof IcapChunk) {
    		// TODO add the to be received body into the respective http message
    	} else {
    		ctx.sendUpstream(e);
    	}
    }
}
