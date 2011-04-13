package ch.mimo.netty.handler.codec.icap.socket;

import static org.jboss.netty.channel.Channels.write;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

public class TrickleDownstreamHandler implements ChannelDownstreamHandler {

	private long latency;
	private int chunkSize;
	
	public TrickleDownstreamHandler(long latency, int chunkSize) {
		this.latency = latency;
		this.chunkSize = chunkSize;
	}
	
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if(e instanceof MessageEvent) {
			MessageEvent event = (MessageEvent)e;
			ChannelBuffer buffer = (ChannelBuffer)event.getMessage();
			while(buffer.readableBytes() > 0) {
				ChannelBuffer newBuffer = ChannelBuffers.dynamicBuffer();
				if(buffer.readableBytes() >= chunkSize) {
					newBuffer.writeBytes(buffer.readBytes(chunkSize));
				} else {
					newBuffer.writeBytes(buffer.readBytes(buffer.readableBytes()));
				}
				Thread.sleep(latency);
				write(ctx, e.getFuture(),newBuffer,event.getRemoteAddress());
			}
		} else {
			ctx.sendDownstream(e);
		}
	}

}
