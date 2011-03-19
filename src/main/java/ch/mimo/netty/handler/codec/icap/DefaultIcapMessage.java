package ch.mimo.netty.handler.codec.icap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.internal.StringUtil;


public class DefaultIcapMessage implements IcapMessage {
	
	private IcapHeaders icapHeaders;
	private HttpVersion version;
	private HttpMethod method;
	private String uri;
	private Encapsulated encapsulated;
	
	private HttpRequest httpRequest;
	private HttpResponse httpResponse;
	
	private HttpChunk preview;
	
	public DefaultIcapMessage(HttpVersion version) {
		setProtocolVersion(version);
		icapHeaders = new IcapHeaders();
	}
	
    public DefaultIcapMessage(HttpVersion icapVersion, HttpMethod method, String uri) {
        this(icapVersion);
        setMethod(method);
        setUri(uri);
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
		icapHeaders.addHeader(name,value);
	}

	@Override
	public void setHeader(String name, Object value) {
		icapHeaders.setHeader(name,value);
	}

	@Override
	public void setHeader(String name, Iterable<?> values) {
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
	public boolean isPreviewMessage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsHttpRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsHttpRequestBody() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsHttpResponseBody() {
		// TODO Auto-generated method stub
		return false;
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
	
	public boolean isPreview() {
		// TODO remove literal
		return containsHeader("Preview");
	}
	
	public void setPreview(HttpChunk chunk) {
		this.preview = chunk;
	}
	
	public HttpChunk getPreview() {
		return preview;
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
        }
        
        if(httpResponse != null) {
        	buf.append("--- encapsulated HTTP Response ---").append(StringUtil.NEWLINE);
        	buf.append(httpResponse.toString());
        }
        
        if(getPreview() != null) {
        	buf.append("--- Preview ---");
        	buf.append(preview.toString());
        }
        
        // Remove the last newline.
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
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
