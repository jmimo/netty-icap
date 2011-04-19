package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class IcapChunkSeparator extends SimpleChannelUpstreamHandler {

	private int chunkSize;
	
	public IcapChunkSeparator(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	Object msg = e.getMessage();
    	if(msg instanceof IcapMessage) {
    		IcapMessage message = (IcapMessage)msg;
    		// TODO handle preview
    		Channels.fireMessageReceived(ctx,message,e.getRemoteAddress());
    		ChannelBuffer content = null;
    		if(message.getHttpRequest().getContent() != null && message.getHttpRequest().getContent().readableBytes() > 0) {
    			content = message.getHttpRequest().getContent();
    			message.setBody(IcapMessageElementEnum.REQBODY);
    		} else if(message.getHttpResponse().getContent() != null && message.getHttpRequest().getContent().readableBytes() > 0) {
    			content = message.getHttpResponse().getContent();
    			message.setBody(IcapMessageElementEnum.RESBODY);
    		}
    		// TODO implement options body for this we need to introduce a ChannelBuffer member in the IcapMessage implementation.
    		if(content != null) {
    			boolean dataWasSent = false;
				while(content.readableBytes() > 0) {
					IcapChunk chunk = null;
					if(content.readableBytes() > chunkSize) {
						chunk = new DefaultIcapChunk(content.readBytes(chunkSize));
					} else {
						chunk = new DefaultIcapChunk(content.readBytes(content.readableBytes()));
					}
					// TODO handle preview chunks
					Channels.fireMessageReceived(ctx,chunk,e.getRemoteAddress());
					dataWasSent = true;
				}
				if(dataWasSent) {
					IcapChunkTrailer trailer = new DefaultIcapChunkTrailer();
					// TODO handle preview
					// TODO we are currently unable to handle trailing headers. for this we have to specify in the message that there are 
					// trailing headers and what they are named.
					Channels.fireMessageReceived(ctx,trailer,e.getRemoteAddress());
				}
    		} else {
    			ctx.sendUpstream(e);
    		}
    	} else {
    		ctx.sendUpstream(e);
    	}
    }
}
