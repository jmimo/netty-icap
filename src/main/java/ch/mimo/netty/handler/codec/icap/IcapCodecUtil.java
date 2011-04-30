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

import java.nio.charset.Charset;


/**
 * This class is an exact copy of @see HttpCodecUtil
 * Once the ICAP codec will be integrated into netty this has to be consolidated.
 */
public final class IcapCodecUtil {
	
	// 0; ieof
	public static final Byte[] IEOF_SEQUENCE = new Byte[]{48,59,32,105,101,111,102};
	
	// TODO find other solution...
	public static final byte[] NATIVE_IEOF_SEQUENCE = new byte[]{48,59,32,105,101,111,102};
	
	public static final String IEOF_SEQUENCE_STRING = "0; ieof";
	
    //space ' '
	public static final byte SPACE = 32;

    //tab ' '
//	public static final byte TAB = 9;

    /**
     * Carriage return
     */
	public static final byte CR = 13;

    /**
     * Equals '='
     */
//	public static final byte EQUALS = 61;

    /**
     * Line feed character
     */
	public static final byte LF = 10;

    /**
     * carriage return line feed
     */
	public static final byte[] CRLF = new byte[] { CR, LF };

    /**
    * Colon ':'
    */
	public static final byte COLON = 58;

    /**
    * Semicolon ';'
    */
//	public static final byte SEMICOLON = 59;

     /**
    * comma ','
    */
//	public static final byte COMMA = 44;

//	public static final byte DOUBLE_QUOTE = '"';

//	public static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
    
	public static final Charset ASCII_CHARSET = Charset.forName("ASCII");
    
	public static final String ENCAPSULATION_ELEMENT_REQHDR = "req-hdr";
	public static final String ENCAPSULATION_ELEMENT_RESHDR = "res-hdr";
	public static final String ENCAPSULATION_ELEMENT_REQBODY = "req-body";
	public static final String ENCAPSULATION_ELEMENT_RESBODY = "res-body";
	public static final String ENCAPSULATION_ELEMENT_OPTBODY = "opt-body";
	public static final String ENCAPSULATION_ELEMENT_NULLBODY = "null-body";
    
    private IcapCodecUtil() {
    }

    // TODO could also be in IcapHeader class
    public static void validateHeaderName(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        for (int i = 0; i < name.length(); i ++) {
            char caracter = name.charAt(i);
            if (caracter > 127) {
                throw new IllegalArgumentException("name contains non-ascii character: " + name);
            }

            // Check prohibited characters.
            switch (caracter) {
            case '\t': case '\n': case 0x0b: case '\f': case '\r':
            case ' ':  case ',':  case ':':  case ';':  case '=':
                throw new IllegalArgumentException("name contains one of the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + name);
            }
        }
    }

    // TODO could also be in IcapHeader class
    public static void validateHeaderValue(String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }

        // 0 - the previous character was neither CR nor LF
        // 1 - the previous character was CR
        // 2 - the previous character was LF
        int state = 0;

        for (int i = 0; i < value.length(); i ++) {
            final char caracter = value.charAt(i);

            // Check the absolutely prohibited characters.
            if(caracter == 0x0b | caracter == '\f') {
            	throw new IllegalArgumentException("value contains a prohibited character " + caracter + ": " + value);
            }

            // Check the CRLF (HT | SP) pattern
            if(state == 0) {
            	if(caracter == '\r') {
            		state = 1;
            	}
            	if(caracter == '\n') {
            		state = 2;
            	}
            } else if(state == 1) {
            	if(caracter == '\n') {
            		state = 2;
            	} else {
            		throw new IllegalArgumentException("Only '\\n' is allowed after '\\r': " + value);
            	}
            } else if(state == 2) {
            	if(caracter == '\t') {
            		state = 0;
            	} else {
            		throw new IllegalArgumentException("Only ' ' and '\\t' are allowed after '\\n': " + value);
            	}
            }
        }

        if (state != 0) {
            throw new IllegalArgumentException("value must not end with '\\r' or '\\n':" + value);
        }
    }
}
