/*******************************************************************************
 * Copyright 2012 Michael Mimo Moratti
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ch.mimo.netty.handler.codec.icap;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * This class parses, creates and provides the very important Encapsulated header to the Decoder and
 * encapsulates the complexity of that header.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public final class Encapsulated {
	
	private List<Entry> entries;
	
	public Encapsulated() {
		entries = new ArrayList<Encapsulated.Entry>();
	}
	
	/**
	 * Creates an instance based on the value given.
	 * 
	 * @param headerValue valid Encapsulated value.
	 */
	public Encapsulated(String headerValue) {
		this();
		parseHeaderValue(headerValue);
	}
	
	/**
	 * Gets whether a given entry exists in the header value.
	 * 
	 * @param entity the entity such as REQHDR, RESHDR and so on.
	 * @return boolean true if the entity in question is present.
	 */
	public boolean containsEntry(IcapMessageElementEnum entity) {
		for(Entry entry : entries) {
			if(entry.getName().equals(entity)) {
				return true;
			}
		}
		return false;
 	}
	
	/**
	 * Gets whether the message contains a body entity. This can be any valid body entry
	 * including the null-body entity which indicates that the message does not contain.
	 * a body.
	 * 
	 * @return the correct @see {@link IcapMessageElementEnum} value.
	 */
	public IcapMessageElementEnum containsBodyEntry() {
		IcapMessageElementEnum body = null;
		for(Entry entry : entries) {
			if(entry.getName().equals(IcapMessageElementEnum.OPTBODY)) {
				body = entry.getName();
				break;
			} else if(entry.getName().equals(IcapMessageElementEnum.REQBODY)) {
				body = entry.getName();
				break;
			} else if(entry.getName().equals(IcapMessageElementEnum.RESBODY)) {
				body = entry.getName();
				break;
			} else if(entry.getName().equals(IcapMessageElementEnum.NULLBODY)) {
				body = entry.getName();
				break;
			}
		}
		return body;
	}
	
	/**
	 * Iterator method. This method will provide the next available
	 * Entry. Eventually it will return null.
	 * Whether to return the next valid entry in the list dependens on the
	 * @see Encapsulated#setEntryAsProcessed(IcapMessageElementEnum) method.
	 * 
	 * @return @see {@link IcapMessageElementEnum} or null if no more entries are available.
	 */
	public IcapMessageElementEnum getNextEntry() {
		IcapMessageElementEnum entryName = null;
		for(Entry entry : entries) {
			if(!entry.isProcessed()) {
				entryName = entry.getName();
				break;
			}
		}
		return entryName;
	}
	
	/**
	 * reports that a given entry was processed and that the @see Encapsulated#getNextEntry()
	 * can now return the next entry in line or null if no more are present.
	 * 
	 * @param entryName the entry that was procesed.
	 */
	public void setEntryAsProcessed(IcapMessageElementEnum entryName) {
		Entry entry = getEntryByName(entryName);
		if(entry != null) {
			entry.setIsProcessed();
		}
	}
	
	/**
	 * Sets an entry with it's corresponding position.
	 * 
	 * @param name the name of the Entry.
	 * @param position the position of the entry within the icap message.
	 */
	public void addEntry(IcapMessageElementEnum name, int position) {
		Entry entry = new Entry(name,position);
		entries.add(entry);
	}
	
	/**
	 * This method encodes an {@link Encapsulated} instance into an Icap Message.
	 * 
	 * @param buffer the Channelbuffer to encode into.
	 * @return the amount of bytes that where written to the buffer while encoding.
	 * @throws UnsupportedEncodingException If a character cannot be encoded in ASCII.
	 */
	public int encode(ChannelBuffer buffer) throws UnsupportedEncodingException {
		int index = buffer.readableBytes();
		Collections.sort(entries);
		buffer.writeBytes("Encapsulated: ".getBytes(IcapCodecUtil.ASCII_CHARSET));
		Iterator<Entry> entryIterator = entries.iterator();
		while(entryIterator.hasNext()) {
			Entry entry = entryIterator.next();
			buffer.writeBytes(entry.getName().getValue().getBytes(IcapCodecUtil.ASCII_CHARSET));
			buffer.writeBytes("=".getBytes(IcapCodecUtil.ASCII_CHARSET));
			buffer.writeBytes(Integer.toString(entry.getPosition()).getBytes(IcapCodecUtil.ASCII_CHARSET));
			if(entryIterator.hasNext()) {
				buffer.writeByte(',');
				buffer.writeByte(IcapCodecUtil.SPACE);
			}
		}
        buffer.writeBytes(IcapCodecUtil.CRLF);
        buffer.writeBytes(IcapCodecUtil.CRLF);
		return buffer.readableBytes() - index;
	}
	
	/*
	REQMOD request: 	 [req-hdr] req-body
	REQMOD response: 	{[req-hdr] req-body} | {[res-hdr] res-body}
	RESPMOD request:	 [req-hdr] [res-hdr] res-body
	RESPMOD response:	 [res-hdr] res-body
	OPTIONS response:	 opt-body
	 */
	private void parseHeaderValue(String headerValue) {
		if(headerValue == null) {
			throw new IcapDecodingError("No value associated with Encapsualted header");
		}
		StringTokenizer tokenizer = new StringTokenizer(headerValue,",");
		while(tokenizer.hasMoreTokens()) {
			String parameterString = tokenizer.nextToken();
			if(parameterString != null) {
				String[] parameter = splitParameter(parameterString.trim());
				try {
					int value = Integer.parseInt(parameter[1]);
					Entry entry = new Entry(IcapMessageElementEnum.fromString(parameter[0]),value);
					entries.add(entry);
				} catch(NumberFormatException nfe) {
					throw new IcapDecodingError("the Encapsulated header value [" + parameter[1] + "] for the key [" + parameter[0] + "] is not a number");
				}
			}
		}
		Collections.sort(entries);
	}
	
	private String[] splitParameter(String parameter) {
		int offset = parameter.indexOf('=');
		if(offset <= 0) {
			throw new IcapDecodingError("Encapsulated header value was not understood [" + parameter + "]");
		}
		String key = parameter.substring(0,offset);
		String value = parameter.substring(offset + 1,parameter.length());
		if(value.contains(",")) {
			value = value.substring(0,value.indexOf(','));
		}
		return new String[]{key.trim(),value};
	}
	
	private Entry getEntryByName(IcapMessageElementEnum entryName) {
		Entry returnValue = null;
		for(Entry entry : entries) {
			if(entry.getName().equals(entryName)) {
				returnValue = entry;
				break;
			}
		}
		return returnValue;
	}
	
	private final static class Entry implements Comparable<Entry> {

		private final IcapMessageElementEnum name;
		private final Integer position;
		private boolean processed;
		
		public Entry(IcapMessageElementEnum name, Integer position) {
			this.name = name;
			this.position = position;
		}
		
		public IcapMessageElementEnum getName() {
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
			if(this.name.equals(IcapMessageElementEnum.NULLBODY)) {
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
