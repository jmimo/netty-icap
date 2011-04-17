package ch.mimo.netty.handler.codec.icap;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JDKLoggerConfiguration {

	private LogManager logManager;
	private Logger rootLogger;
	private final Handler defaultHandler = new ConsoleHandler();
	private final Formatter defaultFormatter = new SimpleFormatter();
	
	public JDKLoggerConfiguration() {
	    this.logManager = LogManager.getLogManager();
	    this.rootLogger = Logger.getLogger("");

	    configure();
	}

	private final void configure() {
		defaultHandler.setFormatter(defaultFormatter);
		if(Boolean.valueOf(System.getProperty(AbstractIcapTest.TEST_OUTPUT))) {
			defaultHandler.setLevel(Level.ALL);
			rootLogger.setLevel(Level.ALL);
		} else {
			defaultHandler.setLevel(Level.ALL);
			rootLogger.setLevel(Level.ALL);
		}
		rootLogger.addHandler(defaultHandler);
		logManager.addLogger(rootLogger);
	}
}
