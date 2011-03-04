package ch.mimo.netty.handler.codec.icap;

import java.util.StringTokenizer;

public class Encapsulated {
	
	public static final String REQHDR 	= "req-hdr";
	public static final String RESHDR 	= "res-hdr";
	public static final String REQBODY 	= "req-body";
	public static final String RESBODY 	= "res-body";
	public static final String OPTBODY 	= "opt-body";
	public static final String NULLBODY = "null-body";
	
	private String headerValue;
	private Entry[] entries;
	
	public Encapsulated() {
		entries = new Entry[3];
	}
	
	public static Encapsulated parseHeader(String headerValue) {
		Encapsulated encapsulated = new Encapsulated();
		encapsulated.parseHeaderValue(headerValue);
		return encapsulated;
	}
	
	public int getPosition(String parameter) {
		for(Entry entry : entries) {
			if(entry.getName().equals(parameter)) {
				return entry.getPosition();
			}
		}
		return -1;
	}
	
	/*
	REQMOD request: 	 [req-hdr] req-body
	REQMOD response: 	{[req-hdr] req-body} | {[res-hdr] res-body}
	RESPMOD request:	 [req-hdr] [res-hdr] res-body
	RESPMOD response:	 [res-hdr] res-body
	OPTIONS response:	 opt-body
	 */
	public void parseHeaderValue(String headerValue) {
		StringTokenizer tokenizer = new StringTokenizer(headerValue,",");
		int counter = 0;
		while(tokenizer.hasMoreTokens()) {
			if(counter > 3) {
				throw new Error("There are more than 3 Encapsulation values!");
			}
			String parameterString = tokenizer.nextToken();
			if(parameterString != null) {
				String[] parameter = splitParameter(parameterString.trim());
				try {
					int value = Integer.parseInt(parameter[1]);
					entries[counter++] = new Entry(parameter[0],value);
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
	
	private class Entry {

		private String name;
		private Integer position;
		
		public Entry(String name, Integer position) {
			this.name = name;
			this.position = position;
		}
		
		public String getName() {
			return name;
		}
		
		public Integer getPosition() {
			return position;
		}
	}
}
