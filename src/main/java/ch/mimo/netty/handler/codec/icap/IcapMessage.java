package ch.mimo.netty.handler.codec.icap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;

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
    List<String> getHeaders(String name);

    /**
     * Returns the all header names and values that this message contains.
     *
     * @return the {@link List} of the header name-value pairs.  An empty list
     *         if there is no header in this message.
     */
    List<Map.Entry<String, String>> getHeaders();

    /**
     * @param header name
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
     */
    void addHeader(String name, Object value);

    /**
     * Sets a new header with the specified name and value.  If there is an
     * existing header with the same name, the existing header is removed.
     * @param name header name
     * @param value for the given name
     */
    void setHeader(String name, Object value);

    /**
     * Sets a new header with the specified name and values.  If there is an
     * existing header with the same name, the existing header is removed.
     * @param name header name
     * @param values for the given name
     */
    void setHeader(String name, Iterable<?> values);

    /**
     * Removes the header with the specified name.
     */
    void removeHeader(String name);

    /**
     * Removes all headers from this message.
     */
    void clearHeaders();
    
    /**
     * @return the protocol version of this message.
     */
    HttpVersion getProtocolVersion();

    /**
     * Sets the protocol version of this message.
     * @param version @see {@link HttpVersion}
     */
    void setProtocolVersion(HttpVersion version);
    
    /**
     * @return whether this message is a preview of the actual message.
     */
    boolean isPreviewMessage();
	
    /**
     * @return true if a http request was delivered.
     */
    boolean containsHttpRequest();
    
    /**
     * @return true if a http request body was delivered.
     */
    boolean containsHttpRequestBody();
    
    /**
     * @return the actual http request instance @see {@link HttpRequest}
     */
	HttpRequest getHttpRequest();
	
	void setHttpRequest(HttpRequest httpRequest);
	
	/**
	 * @return true if a http response was delivered.
	 */
	boolean containsHttpResponse();
	
	/**
	 * @return return true if a http response body was delivered.
	 */
	boolean containsHttpResponseBody();
	
	/**
	 * @return the actual http response instance @see {@link HttpResponse}
	 */
	HttpResponse getHttpResponse();
	
	void setHttpResponse(HttpResponse response);
	
	void setMethod(HttpMethod method);

	HttpMethod getMethod();
	
	void setUri(String uri);
	
	String getUri();
	
	void setEncapsulatedHeader(Encapsulated encapsulated);
	
	Encapsulated getEncapsulatedHeader();
	
	boolean isPreview();
	
	void setPreview(HttpChunk chunk);
	
	HttpChunk getPreview();
}
