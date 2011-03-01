package ch.mimo.netty.handler.codec.icap;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;


public class DefaultIcapMessage implements IcapMessage {
	
	private IcapVersion version;
	
	public DefaultIcapMessage() {
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entry<String, String>> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsHeader(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addHeader(String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeader(String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeader(String name, Iterable<?> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeHeader(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearHeaders() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}
}
