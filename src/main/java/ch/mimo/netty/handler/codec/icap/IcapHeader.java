package ch.mimo.netty.handler.codec.icap;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class IcapHeader {
	
	private Entry base;
	private Entry head;
	
	public static final class Names {
        /**
         * {@code "Cache-Control"}
         */
		public static final String CACHE_CONTROL = "Cache-Control";
        /**
         * {@code "Connection"}
         */
		public static final String CONNECTION = "Connection";
        /**
         * {@code "Date"}
         */
		public static final String DATE = "Date";	
        /**
         * {@code "Expires"}
         */
		public static final String EXPIRES = "Expires";
        /**
         * {@code "Pragma"}
         */
		public static final String PRAGMA = "Pragma";
        /**
         * {@code "Trailer"}
         */
		public static final String TRAILER = "Trailer";
        /**
         * {@code "Upgrade"}
         */
		public static final String UPGRADE = "Upgrade";
        /**
         * {@code "Encapsulated"}
         */
		public static final String ENCAPSULATED = "Encapsulated";
        /**
         * {@code "Authorization"}
         */
		public static final String AUTHORIZATION = "Authorization";
        /**
         * {@code "Allow"}
         */
		public static final String ALLOW = "Allow";
        /**
         * {@code "From"}
         */
		public static final String FROM = "From";
        /**
         * {@code "Host"}
         */
		public static final String HOST = "Host";
        /**
         * {@code "Referer"}
         */
		public static final String REFERER = "Referer";
        /**
         * {@code "User-Agent"}
         */
		public static final String USER_AGENT = "User-Agent";
        /**
         * {@code "Preview"}
         */
		public static final String PREVIEW = "Preview";
	}
	
	public IcapHeader() {
	}
	
	public void clearHeaders() {
		base = null;
		head = null;
	}
	
	public void addHeader(String name, Object value) {
		Entry entry = new Entry(name,value);
		if(base == null) {
			base = entry;
			head = entry;
		} else {
			Entry currentHead = head;
			head = entry;
			entry.before = currentHead;
			currentHead.after = entry;
		}
	}
	
	public void setHeader(String name, Object value) {
		removeHeader(name);
		addHeader(name,value);
	}
	
	public void setHeader(String name, Iterable<?> values) {
		removeHeader(name);
		for(Object value : values) {
			addHeader(name,value);
		}
	}
	
	public String getHeader(String name) {
		Entry entry = base;
		while(entry != null) {
			if(identicalKeys(entry.getKey(),name)) {
				return entry.getValue();
			}
			entry = entry.after;
		}
		return null;
	}
	
	public Set<String> getHeaders(String name) {
		Set<String> values = new LinkedHashSet<String>();
		Entry entry = base;
		while(entry != null) {
			if(identicalKeys(entry.getKey(),name)) {
				values.add(entry.getValue());
			}
			entry = entry.after;
		}
		return values;
	}
	
	public Set<Map.Entry<String, String>> getHeaders() {
		Set<Map.Entry<String, String>> headers = new LinkedHashSet<Map.Entry<String,String>>();
		Entry entry = base;
		while(entry != null) {
			headers.add(entry);
			entry = entry.after;
		}
		return headers;
	}
	
	public boolean containsHeader(String name) {
		return getHeader(name) != null;
	}
	
	public void removeHeader(String name) {
		if(base.after == null) {
			base = null;
			return;
		}
		Entry entry = base.after;
		while(entry != null) {
			if(identicalKeys(entry.getKey(),name)) {
				Entry before = entry.before;
				Entry after = entry.after;
				before.after = after;
				after.before = before;
				entry = after;
			} else {
				entry = entry.after;
			}
		}
		if(identicalKeys(base.getKey(),name)) {
			base = base.after;
			base.before = null;
		}
	}
	
	public Set<String> getHeaderNames() {
		Set<String> names = new LinkedHashSet<String>();
		Entry entry = base;
		while(entry != null) {
			names.add(entry.getKey());
			entry = entry.after;
		}
		return names;
	}
	
	public int getPreviewHeaderValue() {
		String value = getHeader(Names.PREVIEW);
		int result = 0;
		try {
			if(value != null) {
				result = Integer.parseInt(value);
			}
		} catch(NumberFormatException nfe) {
			throw new IcapDecodingError("Unable to understand the preview amount value [" + value + "]");
		}
		return result;
	}
	
	private boolean identicalKeys(String key1, String key2) {
		if(key1 != null & key2 != null) {
			return key1.toUpperCase().equals(key2.toUpperCase());
		}
		return false;
	}

	private static final class Entry implements Map.Entry<String, String> {
		
		private String key;
		private String value;
		
		private Entry before, after;
		
		Entry(String key, Object value) {
			this.key = key;
			if(value != null) {
				this.value = value.toString();
			}
		}
		
		public String getKey() {
			return key;
		}
		
		public String getValue() {
			return value;
		}
		
		@Override
		public String setValue(String value) {
			return null;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(String name : getHeaderNames()) {
			builder.append("[" + name + "] = [" + getHeaders(name) + "]");
		}
		return builder.toString();
	}
}
