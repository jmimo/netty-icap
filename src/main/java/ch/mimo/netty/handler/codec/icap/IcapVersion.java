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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * Provides translation and handling for Icap version string.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public final class IcapVersion {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");
	
	private String protocolName;
	private int major;
	private int minor;
	private String text;
	
	public static final IcapVersion ICAP_1_0 = new IcapVersion("ICAP", 1, 0);

	/**
	 * @param protocolName ICAP or different
	 * @param major the major version
	 * @param minor the minor version
	 */
    private IcapVersion(String protocolName, int major, int minor) {
    	this.protocolName = protocolName;
    	this.major = major;
    	this.minor = minor;
    	this.text = protocolName + '/' + major + '.' + minor;
    }
    
    /**
     * parses a valid icap protocol version string.
     * @param text the version (ICAP/1.0)
     */
    private IcapVersion(String text) {
    	if(text == null) {
    		throw new NullPointerException("text");
    	}
        Matcher m = VERSION_PATTERN.matcher(text.trim().toUpperCase());
        if (!m.matches()) {
            throw new IllegalArgumentException("invalid version format: [" + text + "]");
        }
        protocolName = m.group(1);
        major = Integer.parseInt(m.group(2));
        minor = Integer.parseInt(m.group(3));
        this.text = text;
    }
    
    /**
     * Protocol name
     * @return ICAP or different.
     */
    public String getProtocolName() {
    	return protocolName;
    }
    
    /**
     * Major version
     * @return 1
     */
    public int getMajorVersion() {
    	return major;
    }
    
    /**
     * Minor version
     * @return 0
     */
    public int getMinorVersion() {
    	return minor;
    }
    
    /**
     * The text representation of this version.
     * @return ICAP/1.0
     */
    public String getText() {
    	return text;
    }
	
    /**
     * Returns an existing or new {@link HttpVersion} instance which matches to
     * the specified RTSP version string.  If the specified {@code text} is
     * equal to {@code "ICAP/1.0"}, {@link #ICAP_1_0} will be returned.
     * Otherwise, a new {@link HttpVersion} instance will be returned.
     */
    public static IcapVersion valueOf(String text) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        if (text.trim().toUpperCase().equals("ICAP/1.0")) {
            return ICAP_1_0;
        }

        return new IcapVersion(text);
    }
    
    @Override
    public String toString() {
    	return text;
    }
}
