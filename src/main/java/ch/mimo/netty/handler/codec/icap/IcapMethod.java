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

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

/**
 * ICAP methods that are valid to use in messages.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public final class IcapMethod {
	
	/**
	 * Is used as identifier when decoding any response.
	 */
	static final HttpMethod RESPONSE = new HttpMethod("RESPONSE"); 
	
	/**
	 * Request Modification
	 */
	public static final HttpMethod REQMOD = new HttpMethod("REQMOD");
	
	/**
	 * Response Modification
	 */
	public static final HttpMethod RESPMOD = new HttpMethod("RESPMOD");
	
	/**
	 * learn about configuration
	 */
	public static final HttpMethod OPTIONS = HttpMethod.OPTIONS;
	
    private static final Map<String, HttpMethod> METHOD_MAP =
        new HashMap<String, HttpMethod>();

	static {
		METHOD_MAP.put(REQMOD.toString(),REQMOD);
		METHOD_MAP.put(RESPMOD.toString(),RESPMOD);
	    METHOD_MAP.put(OPTIONS.toString(),OPTIONS);
	}
	
	private IcapMethod() {
		super();
	}
	
    /**
     * Returns the {@link HttpMethod} represented by the specified name.
     * If the specified name is a standard RTSP method name, a cached instance
     * will be returned.  Otherwise, a new instance will be returned.
     */
    public static HttpMethod valueOf(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        name = name.trim().toUpperCase();
        if (name.length() == 0) {
            throw new IllegalArgumentException("empty name");
        }

        HttpMethod result = METHOD_MAP.get(name);
        if (result != null) {
            return result;
        } else {
            return new HttpMethod(name);
        }
    }
}
