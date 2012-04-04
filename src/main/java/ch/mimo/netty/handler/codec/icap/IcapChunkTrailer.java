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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Icap chunk trailer.
 * In comparison to Http this will always be the last chunk in any transmission. With or without trailing
 * hedaers.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public interface IcapChunkTrailer extends IcapChunk {

    /**
     * Always returns {@code true}.
     */
    boolean isLast();

    /**
     * Returns the trailing header value with the specified header name.
     * If there are more than one trailing header value for the specified
     * header name, the first value is returned.
     *
     * @return the header value or {@code null} if there is no such header
     *
     */
    String getHeader(String name);

    /**
     * Returns the trailing header values with the specified header name.
     *
     * @return the {@link List} of header values.  An empty list if there is no
     *         such header.
     */
    List<String> getHeaders(String name);

    /**
     * Returns the all header names and values that this trailer contains.
     *
     * @return the {@link List} of the header name-value pairs.  An empty list
     *         if there is no header in this trailer.
     */
    List<Map.Entry<String, String>> getHeaders();

    /**
     * Returns {@code true} if and only if there is a trailing header with
     * the specified header name.
     */
    boolean containsHeader(String name);

    /**
     * Returns the {@link Set} of all trailing header names that this trailer
     * contains.
     */
    Set<String> getHeaderNames();

    /**
     * Adds a new trailing header with the specified name and value.
     */
    void addHeader(String name, Object value);

    /**
     * Sets a new trailing header with the specified name and value.
     * If there is an existing trailing header with the same name, the existing
     * one is removed.
     */
    void setHeader(String name, Object value);

    /**
     * Sets a new trailing header with the specified name and values.
     * If there is an existing trailing header with the same name, the existing
     * one is removed.
     */
    void setHeader(String name, Iterable<?> values);

    /**
     * Removes the trailing header with the specified name.
     */
    void removeHeader(String name);

    /**
     * Removes all trailing headers from this trailer.
     */
    void clearHeaders();
}
