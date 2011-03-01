package ch.mimo.netty.handler.codec.icap;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

public final class IcapMethod {
	
	/**
	 * Request Modification
	 */
	public static final HttpMethod REQMOD = new HttpMethod("REQMOD");
	
	/**
	 * Response Modification
	 */
	public static final HttpMethod RESPMOD = new HttpMethod("RESPMOD");
	
	/**
	 * learn about configuration
	 */
	public static final HttpMethod OPTIONS = HttpMethod.OPTIONS;
	
    private static final Map<String, HttpMethod> methodMap =
        new HashMap<String, HttpMethod>();

	static {
		methodMap.put(REQMOD.toString(),REQMOD);
		methodMap.put(RESPMOD.toString(),RESPMOD);
	    methodMap.put(OPTIONS.toString(),OPTIONS);
	}
	
	private IcapMethod() {
		super();
	}
	
    /**
     * Returns the {@link HttpMethod} represented by the specified name.
     * If the specified name is a standard RTSP method name, a cached instance
     * will be returned.  Otherwise, a new instance will be returned.
     */
    public static HttpMethod valueOf(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        name = name.trim().toUpperCase();
        if (name.length() == 0) {
            throw new IllegalArgumentException("empty name");
        }

        HttpMethod result = methodMap.get(name);
        if (result != null) {
            return result;
        } else {
            return new HttpMethod(name);
        }
    }
}
