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

public class IcapVersionTest extends AbstractIcapTest {

	@Test
	public void getValueOfString() {
		assertEquals("no version found",IcapVersion.ICAP_1_0,IcapVersion.valueOf("ICAP/1.0"));
	}
	
	@Test
	public void getValueFromNull() {
		boolean exception = false;
		try {
			IcapVersion.valueOf(null);
		} catch(NullPointerException npe) {
			exception = true;
		}
		assertTrue("no npe was thrown",exception);
	}
	
	@Test
	public void getValueFromUnknownString() {
		boolean exception = false;
		try {
			IcapVersion.valueOf("ICAP/1.X");
		} catch(IllegalArgumentException iae) {
			exception = true;
		}
		assertTrue("no iae was thrown",exception);
	}
}
