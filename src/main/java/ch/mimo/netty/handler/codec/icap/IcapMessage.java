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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * An ICAP message that contains common operations for @see {@link IcapRequest} and @see {@link IcapResponse}.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public interface IcapMessage {

    /**
     * Returns the header value with the specified header name.  If there are
     * more than one header value for the specified header name, the first
     * value is returned.
     *
     * @return the header value or {@code null} if there is no such header
     *
     */
    String getHeader(String name);

    /**
     * Returns the header values with the specified header name.
     *
     * @return the {@link List} of header values.  An empty list if there is no
     *         such header.
     */
    Set<String> getHeaders(String name);

    /**
     * Returns the all header names and values that this message contains.
     *
     * @return the {@link List} of the header name-value pairs.  An empty list
     *         if there is no header in this message.
     */
    Set<Map.Entry<String, String>> getHeaders();

    /**
     * @param name header name
     * @return {@code true} if and only if there is a header with the specified
     * header name.
     */
    boolean containsHeader(String name);

    /**
     * @return {@link Set} of all header names that this message contains.
     */
    Set<String> getHeaderNames();
    
    /**
     * Adds a new header with the specified name and value.
     * @param name header name
     * @param value for the given name
     * @return self in order to chain the method calls
     */
    IcapMessage addHeader(String name, Object value);

    /**
     * Sets a new header with the specified name and value.  If there is an
     * existing header with the same name, the existing header is removed.
     * @param name header name
     * @param value for the given name
     * @return self in order to chain the method calls
     */
    IcapMessage setHeader(String name, Object value);

    /**
     * Sets a new header with the specified name and values.  If there is an
     * existing header with the same name, the existing header is removed.
     * @param name header name
     * @param values for the given name
     * @return self in order to chain the method calls
     */
    IcapMessage setHeader(String name, Iterable<?> values);

    /**
     * Removes the header with the specified name.
     * @return self in order to chain the method calls
     */
    IcapMessage removeHeader(String name);
    
    /**
     * @return the @see {@link Integer} preview header value.
     */
    int getPreviewAmount();

    /**
     * Removes all headers from this message.
     * @return self in order to chain the method calls
     */
    IcapMessage clearHeaders();
    
    /**
     * @return the protocol version of this message.
     */
    IcapVersion getProtocolVersion();

    /**
     * Sets the protocol version of this message.
     * @param version @see {@link IcapVersion}
     * @return self in order to chain the method calls
     */
    IcapMessage setProtocolVersion(IcapVersion version);
    
    /**
     * @return whether this message is a preview of the actual message.
     */
    boolean isPreviewMessage();
	
    /**
     * @return true if a http request was delivered.
     */
    boolean containsHttpRequest();
    
    /**
     * @return the actual http request instance @see {@link HttpRequest}
     */
	HttpRequest getHttpRequest();
	
	/**
	 * @param httpRequest
	 * @return self in order to chain the method calls
	 */
	IcapMessage setHttpRequest(HttpRequest httpRequest);
	
	/**
	 * @return true if a http response was delivered.
	 */
	boolean containsHttpResponse();
	
	/**
	 * @return the actual http response instance @see {@link HttpResponse}
	 */
	HttpResponse getHttpResponse();
	
	/**
	 * Adds a @see {@link HttpResponse} to the Icap message.
	 * 
	 * @param response the @see {@link HttpResponse}
	 * @return self in order to chain the method calls
	 */
	IcapMessage setHttpResponse(HttpResponse response);
	
	/**
	 * Sets the @see {@link Encapsulated} Encapsulation header for this message
	 * @param encapsulated @see {@link Encapsulated} instance
	 * @return self in order to chain the method calls
	 */
	IcapMessage setEncapsulatedHeader(Encapsulated encapsulated);
	
	/**
	 * @return @see {@link Encapsulated} Encapsulated header value
	 */
	Encapsulated getEncapsulatedHeader();

	/**
	 * Sets the indication that this icap message contains a body of some kind.
	 * @param body @see {@link IcapMessageElementEnum}
	 * @return self in order to chain the method calls
	 */
	IcapMessage setBody(IcapMessageElementEnum body);

	/**
	 * @return @see {@link IcapMessageElementEnum} message body indicator.
	 */
	IcapMessageElementEnum getBody();
}
