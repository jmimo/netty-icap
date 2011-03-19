package ch.mimo.netty.handler.codec.icap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Encapsulated {
	
	public static enum EntryName {
		REQHDR("req-hdr"),
		RESHDR("res-hdr"),
		REQBODY("req-body"),
		RESBODY("res-body"),
		OPTBODY("opt-body"),
		NULLBODY("null-body");
		
		private String value;
		
		EntryName(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public static EntryName fromString(String value) {
			if(value != null) {
				for(EntryName entryName : EntryName.values()) {
					if(value.equalsIgnoreCase(entryName.getValue())) {
						return entryName;
					}
				}
			}
			return null;
		}
	}
	
	private String headerValue;
	private List<Entry> entries;
//	private Entry[] entries;
	private int bufferOffsetIndex;
	
	public Encapsulated() {
		entries = new ArrayList<Encapsulated.Entry>();
	}
	
	public static Encapsulated parseHeader(String headerValue) {
		Encapsulated encapsulated = new Encapsulated();
		encapsulated.parseHeaderValue(headerValue);
		return encapsulated;
	}
	
	public boolean containsEntry(EntryName entity) {
		for(Entry entry : entries) {
			if(entry.getName().equals(entity)) {
				return true;
			}
		}
		return false;
 	}
	
	public boolean containsBody() {
		return containsEntry(EntryName.REQBODY) | 
					containsEntry(EntryName.RESBODY) | 
					containsEntry(EntryName.OPTBODY);
	}
	
	public EntryName getNextEntry() {
		for(Entry entry : entries) {
			if(!entry.isProcessed()) {
				return entry.getName();
			}
		}
		return null;
	}
	
	public void setProcessed(EntryName entryName) {
		Entry entry = getEntryByName(entryName);
		entry.setIsProcessed();
	}
	
//	public int getPosition(EntryName entryName) {
//		Entry entry = getEntryByName(entryName);
//		return entry.getPosition();
//	}
	
	/*
	REQMOD request: 	 [req-hdr] req-body
	REQMOD response: 	{[req-hdr] req-body} | {[res-hdr] res-body}
	RESPMOD request:	 [req-hdr] [res-hdr] res-body
	RESPMOD response:	 [res-hdr] res-body
	OPTIONS response:	 opt-body
	 */
	public void parseHeaderValue(String headerValue) {
		if(headerValue == null) {
			throw new Error("No value associated with Encapsualted header");
		}
		StringTokenizer tokenizer = new StringTokenizer(headerValue,",");
		while(tokenizer.hasMoreTokens()) {
			String parameterString = tokenizer.nextToken();
			if(parameterString != null) {
				String[] parameter = splitParameter(parameterString.trim());
				try {
					int value = Integer.parseInt(parameter[1]);
					entries.add(new Entry(EntryName.fromString(parameter[0]),value));
				} catch(NumberFormatException nfe) {
					throw new Error("the Encapsulated header value [" + parameter[1] + "] for the key [" + parameter[0] + "] is not a number");
				}
			}
		}
		Collections.sort(entries);
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
	
	private Entry getEntryByName(EntryName entryName) {
		for(Entry entry : entries) {
			if(entry.getName().equals(entryName)) {
				return entry;
			}
		}
		return null;
	}
	
	private class Entry implements Comparable<Entry> {

		private EntryName name;
		private Integer position;
		private boolean processed;
		
		public Entry(EntryName name, Integer position) {
			this.name = name;
			this.position = position;
		}
		
		public EntryName getName() {
			return name;
		}
		
		public Integer getPosition() {
			return position;
		}
		
		public void setIsProcessed() {
			processed = true;
		}
		
		public boolean isProcessed() {
			return processed;
		}
		
		@Override
		public int compareTo(Entry entry) {
			return this.position.compareTo(entry.position);
		}
		
		@Override
		public String toString() {
			return name + "=" + position + " : " + processed;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Encapsulated: ");
		for(Entry entry : entries) {
			if(entry != null) {
				builder.append(" [").append(entry.toString()).append("] ");
			}
		}
 		return builder.toString();
	}

	public void setBufferOffsetIndex(int bufferOffsetIndex) {
		this.bufferOffsetIndex = bufferOffsetIndex;
	}

	public int getBufferOffsetIndex() {
		return bufferOffsetIndex;
	}
}
