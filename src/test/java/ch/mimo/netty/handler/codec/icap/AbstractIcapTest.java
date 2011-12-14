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

import java.util.logging.Logger;

import org.junit.Before;

public abstract class AbstractIcapTest extends AbstractJDKLoggerPreparation {

	public static final String TEST_OUTPUT = "icap.test.output";

	private static final Logger LOG = Logger.getLogger("TestOutputLogger");
	
	private boolean isOutput;
	
	@Before
	public void validateIfOutputIsDesired() {
		isOutput = Boolean.valueOf(System.getProperty(TEST_OUTPUT));
	}
	
	protected void toggleOutputOn() {
		isOutput = true;
	}
	
	protected void doOutput(String content) {
		if(isOutput) {
			LOG.info("------------------------------------------------------");
			LOG.info("[" + content + "]");
			LOG.info("------------------------------------------------------");
		}
	}
}
