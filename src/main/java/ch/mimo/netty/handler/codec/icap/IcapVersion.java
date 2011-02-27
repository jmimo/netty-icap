package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.handler.codec.http.HttpVersion;

public class IcapVersion extends HttpVersion {

	public static final IcapVersion ICAP_1_0 = new IcapVersion("ICAP", 1, 0, true);

	public IcapVersion(String text, boolean keepAliveDefault) {
		super(text, keepAliveDefault);
	}
	
	public IcapVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault) {
		super(protocolName, majorVersion, minorVersion, keepAliveDefault);
	}
}
