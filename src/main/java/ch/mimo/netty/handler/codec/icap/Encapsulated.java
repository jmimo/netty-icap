/*******************************************************************************
 * Copyright (c) 2011 Michael Mimo Moratti.
 *
 * Michael Mimo Moratti licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package ch.mimo.netty.handler.codec.icap;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jboss.netty.buffer.ChannelBuffer;

public class Encapsulated {
	
	public static enum EntryName {
		// TODO remove literals
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
	
//	public boolean containsBody() {
//		return containsEntry(EntryName.REQBODY) | 
//					containsEntry(EntryName.RESBODY) | 
//					containsEntry(EntryName.OPTBODY);
//	}
	
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
	
	public void addEntry(EntryName name, int position) {
		Entry entry = new Entry(name,position);
		entries.add(entry);
	}
	
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
	
	public int encode(ChannelBuffer buffer) throws UnsupportedEncodingException {
		int index = buffer.readableBytes();
		Collections.sort(entries);
		// TODO remove literal
		buffer.writeBytes("Encapsulated: ".getBytes("ASCII"));
		Iterator<Entry> entryIterator = entries.iterator();
		while(entryIterator.hasNext()) {
			Entry entry = entryIterator.next();
			buffer.writeBytes(entry.getName().getValue().getBytes("ASCII"));
			buffer.writeBytes("=".getBytes("ASCII"));
			buffer.writeBytes(Integer.toString(entry.getPosition()).getBytes("ASCII"));
			if(entryIterator.hasNext()) {
				buffer.writeByte(IcapCodecUtil.SP);
			}
		}
		return buffer.readableBytes() - index;
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
		
		public int getPosition() {
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
			if(entry.getName().equals(EntryName.NULLBODY)) {
				return 1;
			}
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
}
