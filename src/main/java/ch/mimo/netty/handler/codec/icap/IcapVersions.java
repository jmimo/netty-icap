package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.handler.codec.http.HttpVersion;

public class IcapVersions {

	public static final HttpVersion ICAP_1_0 = new HttpVersion("ICAP", 1, 0, true);
	
	private IcapVersions() {
		super();
	}
	
    /**
     * Returns an existing or new {@link HttpVersion} instance which matches to
     * the specified ICAP version string.  If the specified {@code text} is
     * equal to {@code "ICAP/1.0"}, {@link #ICAP_1_0} will be returned.
     * Otherwise, a new {@link HttpVersion} instance will be returned.
     */
    public static HttpVersion valueOf(String text) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        text = text.trim().toUpperCase();
        if (text.equals("ICAP/1.0")) {
            return ICAP_1_0;
        }
        return new HttpVersion(text,true);
    }
}
