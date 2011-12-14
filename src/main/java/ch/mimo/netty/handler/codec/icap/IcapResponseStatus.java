/*******************************************************************************
 * Copyright 2011 - 2012 Michael Mimo Moratti
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

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * ICAP response status enum. contains all valid response codes. like 200, 204 and others.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public enum IcapResponseStatus {
	CONTINUE(100,"Continue"),
	SWITCHING_PROTOCOLS(101,"Switching Protocols"),
	PROCESSING(102,"Processing"),
	OK(200,"OK"),
	CREATED(201,"Created"),
	ACCEPTED(202,"Accepted"),
	NON_AUTHORITATIVE_INFORMATION(203,"Non-Authoritative Information"),
	NO_CONTENT(204,"No Content"),
	RESET_CONTENT(205,"Reset Content"),
	PARTIAL_CONTENT(206,"Partial Content"),
	MULTI_STATUS(207,"Multi-Status"),
	MULTIPLE_CHIOCES(207,"Multiple-Choices"),
	MOVED_PERMANENTLY(301,"Moved Permanently"),
	FOUND(302,"Found"),
	SEE_OTHER(303,"See Other"),
	NOT_MODIFIED(304,"Not Modified"),
	USE_PROXY(305,"Use Proxy"),
	TEMPORARY_REDIRECT(307,"Temporary Redirect"),
	BAD_REQUEST(400,"Bad Request"),
	UNAUTHORIZED(401,"Unauthorized"),
	PAYMENT_REQUIRED(402,"Payment Required"),
	FORBIDDEN(403,"Fobidden"),
	ICAP_SERVICE_NOT_FOUND(404,"ICAP Service not found"),
	METHOD_NOT_ALLOWED(405,"Method not allowed for service"),
	NOT_ACCEPTABLE(406,"Not Acceptable"),
	PROXY_AUTHENTICATION_REQUIRED(407,"Proxy Authentication Required"),
	REQUEST_TIMEOUT(408,"Request timeout"),
	CONFLICT(409,"Conflict"),
	GONE(410,"Gone"),
	LENGTH_REQUIRED(411,"Length Required"),
	PRECONDITION_FAILED(412,"Precondition Failed"),
	REQUEST_ENTITY_TOO_LARGE(413,"Request Entity Too Large"),
	REQUEST_URI_TOO_LONG(414,"Request-URI Too Long"),
	UNSUPPORTED_MEDIA_TYPE(415,"Unsupported Media Type"),
	REQUESTED_RANGE_NOT_SATISFIABLE(416,"Requested Range Not Satisfiable"),
	EXPECTATION_FAILED(417,"Expectation Failed"),
	UNPROCESSABLE_ENTITY(422,"Unprocessable Entity"),
	LOCKED(423,"Locked"),
	FAILED_DEPENDENCY(424,"Failed Dependency"),
	UNORDERED_COLLECTION(425,"Unordered Collection"),
	UPGRADE_REQUIRED(426,"Upgrade Required"),
	SERVER_ERROR(500,"Server error"),
	NOT_IMPLEMENTED(501,"Not Implemented"),
	BAD_GATEWAY(502,"Bad Gateway"),
	SERVICE_UNAVAILABLE(503,"Service Unavailable"),
	GATEWAY_TIMEOUT(504,"Gateway Timeout"),
	ICAP_VERSION_NOT_SUPPORTED(505,"ICAP Version Not Supported"),
	VARIANT_ALSO_NEGOTIATES(506,"Variant Also Negotiates"),
	INSUFFICIENT_STORAGE(507,"Insufficient Storage"),
	NOT_EXTENDED(510,"Not Extended");

	
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
		buffer.writeByte(IcapCodecUtil.SPACE);
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
