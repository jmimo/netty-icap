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

import org.junit.Test;

public class IcapCodecUtilTest extends AbstractIcapTest {

	@Test
	public void validateHeaderName() {
		boolean exception = false;
		try {
			IcapCodecUtil.validateHeaderName("Hel;lo");
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderNameWithInvalidCharacter() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)128);
			builder.append("World");
			IcapCodecUtil.validateHeaderName(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValue() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.CR);
			builder.append((char)IcapCodecUtil.CR);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithOneLineFeed() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.LF);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithOneCarriageReturn() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.CR);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithlastCharacterBeeingCarriageReturn() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.CR);
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithlastCharacterBeeingLineFeed() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)IcapCodecUtil.LF);
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithProbhibitedCharacter() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append('\f');
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
	
	@Test
	public void validateHeaderValueWithOtherProbhibitedCharacter() {
		boolean exception = false;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Hello");
			builder.append((char)0x0b);
			builder.append("World");
			IcapCodecUtil.validateHeaderValue(builder.toString());
		} catch(IllegalArgumentException iage) {
			exception = true;
		}
		assertTrue("no exception was thrown",exception);
	}
}
