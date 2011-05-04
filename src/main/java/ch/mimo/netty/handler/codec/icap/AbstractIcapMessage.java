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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.internal.StringUtil;

/**
 * This is the main Icap message implementation where 
 * all common @see {@link DefaultIcapRequest} and @see {@link DefaultIcapResponse} member are present.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public abstract class AbstractIcapMessage implements IcapMessage {

	private IcapHeader icapHeader;
	private IcapVersion version;
	private IcapMethod method;
	private String uri;
	private Encapsulated encapsulated;
	
	private HttpRequest httpRequest;
	private HttpResponse httpResponse;
	
	private IcapMessageElementEnum body;
	
	public AbstractIcapMessage(IcapVersion version) {
		this.version = version;
		icapHeader = new IcapHeader();
	}
	
    public AbstractIcapMessage(IcapVersion icapVersion, IcapMethod method, String uri) {
    	this(icapVersion);
    	this.method = method;
    	this.uri = uri;
    }

	@Override
	public String getHeader(String name) {
		return icapHeader.getHeader(name);
	}

	@Override
	public Set<String> getHeaders(String name) {
		return icapHeader.getHeaders(name);
	}

	@Override
	public Set<Entry<String, String>> getHeaders() {
		return icapHeader.getHeaders();
	}

	@Override
	public boolean containsHeader(String name) {
		return icapHeader.containsHeader(name);
	}

	@Override
	public Set<String> getHeaderNames() {
		return icapHeader.getHeaderNames();
	}

	@Override
	public void addHeader(String name, Object value) {
		icapHeader.addHeader(name,value);
	}

	@Override
	public void setHeader(String name, Object value) {
		icapHeader.setHeader(name,value);
	}

	@Override
	public void setHeader(String name, Iterable<?> values) {
		icapHeader.setHeader(name,values);
	}

	@Override
	public void removeHeader(String name) {
		icapHeader.removeHeader(name);
	}
	
	@Override
	public int getPreviewAmount() {
		return icapHeader.getPreviewHeaderValue();
	}

	@Override
	public void clearHeaders() {
		icapHeader.clearHeaders();
	}

	@Override
	public IcapVersion getProtocolVersion() {
		return version;
	}

	@Override
	public void setProtocolVersion(IcapVersion version) {
		this.version = version;
	}

	@Override
	public boolean containsHttpRequest() {
		return httpRequest != null;
	}

	@Override
	public HttpRequest getHttpRequest() {
		return httpRequest;
	}
	
	@Override
	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	@Override
	public boolean containsHttpResponse() {
		return httpResponse != null;
	}

	@Override
	public HttpResponse getHttpResponse() {
		return httpResponse;
	}
	
	public void setHttpResponse(HttpResponse response) {
		this.httpResponse = response;
	}

	public void setMethod(IcapMethod method) {
		this.method = method;
	}

	public IcapMethod getMethod() {
		return method;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	@Override
	public void setEncapsulatedHeader(Encapsulated encapsulated) {
		this.encapsulated = encapsulated;
	}

	@Override
	public Encapsulated getEncapsulatedHeader() {
		return encapsulated;
	}
	
	@Override
	public boolean isPreviewMessage() {
		return icapHeader.getPreviewHeaderValue() > 0;
	}
	
	public void setBody(IcapMessageElementEnum body) {
		this.body = body;
	}

	public IcapMessageElementEnum getBody() {
		if(encapsulated != null) {
			return encapsulated.containsBodyEntry();
		} else {
			return body;
		}
	}
	
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getClass().getSimpleName());
        buf.append("(version: ");
        buf.append(getProtocolVersion().getText());
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
        appendHeaders(buf);

        if(httpRequest != null) {
        	buf.append("--- encapsulated HTTP Request ---").append(StringUtil.NEWLINE);
        	buf.append(httpRequest.toString());
        	if(httpRequest.getContent() != null && httpRequest.getContent().readableBytes() > 0) {
        		buf.append(StringUtil.NEWLINE).append("--> HTTP Request contains [" + httpRequest.getContent().readableBytes() + "] bytes of data").append(StringUtil.NEWLINE);
        	}
        }
        
        if(httpResponse != null) {
        	buf.append("--- encapsulated HTTP Response ---").append(StringUtil.NEWLINE);
        	buf.append(httpResponse.toString());
        	if(httpResponse.getContent() != null && httpResponse.getContent().readableBytes() > 0) {
        		buf.append(StringUtil.NEWLINE).append("--> HTTP Response contains [" + httpResponse.getContent().readableBytes() + "] bytes of data").append(StringUtil.NEWLINE);;
        	}
        }
        
        if(isPreviewMessage()) {
        	buf.append("--- Preview ---").append(StringUtil.NEWLINE);
        	buf.append("Preview size: " + icapHeader.getPreviewHeaderValue());
        }
        
        return buf.toString();
    }
    
    private void appendHeaders(StringBuilder buf) {
        for (Map.Entry<String, String> e: getHeaders()) {
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
}