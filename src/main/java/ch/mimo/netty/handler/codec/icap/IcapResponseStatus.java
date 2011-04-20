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

import org.jboss.netty.buffer.ChannelBuffer;

public enum IcapResponseStatus {
	CONTINUE(100,"Continue"),
	OK(200,"OK"),
	NO_CONTENT(204,"No Content");
	
	private String status;
	private int code;
	
	IcapResponseStatus(int code, String status) {
		this.code = code;
		this.status = status;
	}
	
	public int getCode() {
		return code;
	}
	
	public void toResponseInitialLineValue(ChannelBuffer buffer) {
		buffer.writeBytes(Integer.toString(code).getBytes(IcapCodecUtil.ASCII_CHARSET));
		buffer.writeByte(IcapCodecUtil.SP);
		buffer.writeBytes(status.getBytes(IcapCodecUtil.ASCII_CHARSET));
	}
	
	public static IcapResponseStatus fromCode(String code) {
		for(IcapResponseStatus status : IcapResponseStatus.values()) {
			if(Integer.toString(status.getCode()).equalsIgnoreCase(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Unknown Icap response code [" + code + "]");
	}
}
