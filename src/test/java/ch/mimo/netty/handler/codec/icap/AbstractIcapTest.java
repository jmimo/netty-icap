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
