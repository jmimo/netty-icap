/*******************************************************************************
 * Copyright (c) 2012 Michael Mimo Moratti.
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

/**
 * This enum contains all valid ICAP message element names that can occur in
 * an @see {@link Encapsulated} header.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public enum IcapMessageElementEnum {
	REQHDR(IcapCodecUtil.ENCAPSULATION_ELEMENT_REQHDR),
	RESHDR(IcapCodecUtil.ENCAPSULATION_ELEMENT_RESHDR),
	REQBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_REQBODY),
	RESBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_RESBODY),
	OPTBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_OPTBODY),
	NULLBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_NULLBODY);
	
	private String value;
	
	IcapMessageElementEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static IcapMessageElementEnum fromString(String value) {
		if(value != null) {
			for(IcapMessageElementEnum entryName : IcapMessageElementEnum.values()) {
				if(value.equalsIgnoreCase(entryName.getValue())) {
					return entryName;
				}
			}
		}
		return null;
	}
}
