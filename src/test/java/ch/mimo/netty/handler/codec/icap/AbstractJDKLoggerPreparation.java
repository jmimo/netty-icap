package ch.mimo.netty.handler.codec.icap;

import junit.framework.Assert;

import org.junit.BeforeClass;

public class AbstractJDKLoggerPreparation extends Assert {

	@BeforeClass
	public static final void configureJDKLogger() {
		System.setProperty("java.util.logging.config.class","ch.mimo.netty.handler.codec.icap.JDKLoggerConfiguration");
//		try {
//			LogManager.getLogManager().readConfiguration();
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("jdk logger re-configuration failed");
//		}
	}
}
