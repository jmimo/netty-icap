package ch.mimo.netty.handler.codec.icap;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;

import junit.framework.Assert;

public abstract class AbstractIcapTest extends Assert {

	public static final String TEST_OUTPUT = "icap.test.output";

	private static final Logger LOG = Logger.getLogger("TestOutputLogger");
	
	private boolean isOutput;
	
	@BeforeClass
	public static final void configureJDKLogger() {
		System.setProperty("java.util.logging.config.class","ch.mimo.netty.handler.codec.icap.JDKLoggerConfiguration");
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail("jdk logger re-configuration failed");
		} catch (IOException e) {
			e.printStackTrace();
			fail("jdk logger re-configuration failed");
		}
	}
	
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
