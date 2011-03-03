package ch.mimo.netty.handler.codec.icap;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jboss.netty.handler.codec.http.HttpMethod;


public class Encapsulated {
	
	public static final String REQHDR 	= "req-hdr";
	public static final String RESHDR 	= "res-hdr";
	public static final String REQBODY 	= "req-body";
	public static final String RESBODY 	= "res-body";
	public static final String OPTBODY 	= "opt-body";
	public static final String NULLBODY = "null-body";
	
	private HttpMethod requestMethod;
	private String headerValue;
	private Map<String,Integer> parameterMap;
	
	public Encapsulated(HttpMethod requestMethod, String headerValue) {
		this.requestMethod = requestMethod;
		this.headerValue = headerValue;
		parameterMap = new HashMap<String,Integer>();
		init(headerValue);
		validateParameters();
	}
	
	public boolean containsKey(String key) {
		return parameterMap.containsKey(key);
	}
	
	public int getEncapsulatedValue(String key) {
		return parameterMap.get(key);
	}
	
	private void init(String headerValue) {
		StringTokenizer tokenizer = new StringTokenizer(headerValue,",");
		while(tokenizer.hasMoreTokens()) {
			String parameterString = tokenizer.nextToken();
			if(parameterString != null) {
				String[] parameter = splitParameter(parameterString.trim());
				if(parameterMap.containsKey(parameter[0])) {
					throw new Error("Duplicate definition of Encapsulated header value [" + parameter[0] + "]");
				}
				try {
					int value = Integer.parseInt(parameter[1]);
					parameterMap.put(parameter[0],value);
				} catch(NumberFormatException nfe) {
					throw new Error("the Encapsulated header value [" + parameter[1] + "] for the key [" + parameter[0] + "] is not a number");
				}
			}
		}
	}
	
	private String[] splitParameter(String parameter) {
		int offset = parameter.indexOf("=");
		if(offset <= 0) {
			throw new Error("Encapsulated header value was not understood [" + headerValue + "]");
		}
		String key = parameter.substring(0,offset);
		String value = parameter.substring(offset + 1,parameter.length());
		if(value.contains(",")) {
			value = value.substring(0,value.indexOf(","));
		}
		return new String[]{key.trim(),value};
	}
	
	private void validateParameters() {
		if(requestMethod.equals(IcapMethod.REQMOD)) {
			if(!parameterMap.containsKey(REQHDR)) {
				throw new Error("Missing required Encapsulated header value: " + REQHDR);
			}
 		} else if(requestMethod.equals(IcapMethod.RESPMOD)) {
			if(!parameterMap.containsKey(REQHDR)) {
				throw new Error("Missing required Encapsulated header value: " + REQHDR);
			}
			if(!parameterMap.containsKey(RESHDR)) {
				throw new Error("Missing required Encapsulated header value: " + RESHDR);
			}
		} else if(requestMethod.equals(IcapMethod.OPTIONS)) {
			// NOOP there are no mandatory header values for a request of type OPTIONS.
		} else {
			throw new Error("Unknown request method [" + requestMethod + "] this should happen...");
		}
	}
}
