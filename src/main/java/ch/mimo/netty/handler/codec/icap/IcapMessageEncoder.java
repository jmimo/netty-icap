package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public abstract class IcapMessageEncoder extends OneToOneEncoder {

    protected IcapMessageEncoder() {
        super();
    }
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	protected abstract void encodeInitialLine(ChannelBuffer buf, IcapMessage message);
}
