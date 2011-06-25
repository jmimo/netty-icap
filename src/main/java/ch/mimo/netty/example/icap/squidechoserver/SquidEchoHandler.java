package ch.mimo.netty.example.icap.squidechoserver;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import ch.mimo.netty.handler.codec.icap.DefaultIcapResponse;
import ch.mimo.netty.handler.codec.icap.IcapHeaders;
import ch.mimo.netty.handler.codec.icap.IcapMethod;
import ch.mimo.netty.handler.codec.icap.IcapRequest;
import ch.mimo.netty.handler.codec.icap.IcapResponse;
import ch.mimo.netty.handler.codec.icap.IcapResponseStatus;
import ch.mimo.netty.handler.codec.icap.IcapVersion;

/**
 * This handler will enable squid to send all traffic and the responses from this handler will always
 * allow squid to proceed with the current request or response.
 * 
 * The following behavior is standard for this handler:
 * 
 * - OPTIONS: responds 204 is valid TTL 1hr and REQ RESPMOD is accepted.
 * - REQMOD:  HTTP request is returned.
 * - RESPMOD: HTTP response is returned.
 * 
 * - Preview: always responds with 204 No Content. 
 * 
 * This handler was used to integration test together with squid version 3.1.6
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class SquidEchoHandler extends SimpleChannelUpstreamHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object message = e.getMessage();
		IcapResponse response = null;
		if(message instanceof IcapRequest) {
			IcapRequest request = (IcapRequest)message;
			System.out.println("");
			System.out.println("---------------------------- receiving " + request.getMethod() + " ----------------------------");
			System.out.print(message.toString());
			if(request.getMethod().equals(IcapMethod.OPTIONS)) {
				response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
				response.addHeader("Options-TTL","3600");
				response.addHeader("Service-ID","Test Icap Server");
				response.addHeader("Allow","204");
				response.addHeader("Preview","1024");
				response.addHeader("Methods","REQMOD, RESPMOD");
				
			} else if(request.isPreviewMessage()) {
				response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.NO_CONTENT);
			} else {
				response = new DefaultIcapResponse(IcapVersion.ICAP_1_0,IcapResponseStatus.OK);
				response.addHeader(IcapHeaders.Names.ISTAG,"Echo-Server-1.0");
				if(request.getMethod().equals(IcapMethod.REQMOD)) {
					response.setHttpRequest(request.getHttpRequest());
				}
				response.setHttpResponse(request.getHttpResponse());
			}
			System.out.println("");
			System.out.println("---------------------------- sending " + response.getStatus() + " ----------------------------");
			System.out.print(response.toString());
			ctx.getChannel().write(response);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		System.out.println("");
		System.out.println("---------------------------- exception ----------------------------");
		e.getCause().printStackTrace();
	}
}
