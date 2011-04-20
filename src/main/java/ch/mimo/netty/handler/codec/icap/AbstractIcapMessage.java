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
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.internal.StringUtil;


public abstract class AbstractIcapMessage implements IcapMessage {
	
	private IcapHeaders icapHeaders;
	private HttpVersion version;
	private HttpMethod method;
	private String uri;
	private Encapsulated encapsulated;
	
	private HttpRequest httpRequest;
	private HttpResponse httpResponse;
	
	private IcapMessageElementEnum body;
	
	public AbstractIcapMessage(HttpVersion version) {
		this.version = version;
		icapHeaders = new IcapHeaders();
	}
	
    public AbstractIcapMessage(HttpVersion icapVersion, HttpMethod method, String uri) {
    	this(icapVersion);
    	this.method = method;
    	this.uri = uri;
    }

	@Override
	public String getHeader(String name) {
		return icapHeaders.getHeader(name);
	}

	@Override
	public List<String> getHeaders(String name) {
		return icapHeaders.getHeaders(name);
	}

	@Override
	public List<Entry<String, String>> getHeaders() {
		return icapHeaders.getHeaders();
	}

	@Override
	public boolean containsHeader(String name) {
		return icapHeaders.containsHeader(name);
	}

	@Override
	public Set<String> getHeaderNames() {
		return icapHeaders.getHeaderNames();
	}

	@Override
	public void addHeader(String name, Object value) {
		validateHeader(name);
		icapHeaders.addHeader(name,value);
	}

	@Override
	public void setHeader(String name, Object value) {
		validateHeader(name);
		icapHeaders.setHeader(name,value);
	}

	@Override
	public void setHeader(String name, Iterable<?> values) {
		validateHeader(name);
		icapHeaders.setHeader(name,values);
	}

	@Override
	public void removeHeader(String name) {
		icapHeaders.removeHeader(name);
	}

	@Override
	public void clearHeaders() {
		icapHeaders.clearHeaders();
	}

	@Override
	public HttpVersion getProtocolVersion() {
		return version;
	}

	@Override
	public void setProtocolVersion(HttpVersion version) {
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

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public HttpMethod getMethod() {
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
		return containsHeader(IcapHeaders.Names.PREVIEW);
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
	
	protected abstract void validateHeader(String name) throws IllegalArgumentException;
	
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
        }
        
        if(httpResponse != null) {
        	buf.append("--- encapsulated HTTP Response ---").append(StringUtil.NEWLINE);
        	buf.append(httpResponse.toString());
        }
        
        if(isPreviewMessage()) {
        	buf.append("--- Preview ---");
        	buf.append("Preview size: " + getHeader(IcapHeaders.Names.PREVIEW));
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