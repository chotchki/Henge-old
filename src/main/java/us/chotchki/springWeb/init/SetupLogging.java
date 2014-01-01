package us.chotchki.springWeb.init;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

/**
 * Logging setup class for when I decide to get fancy in my logging
 * @author chotchki
 *
 */
public class SetupLogging {
	private static final String pattern = "%date{MM-dd HH:mm:ss} %level %logger{30} %m %ex %n";
	
	private final LoggerContext rootContext;
	private final Logger rootLog;
	
	public SetupLogging(){
		rootLog = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		
		rootContext = rootLog.getLoggerContext();
		rootContext.reset();
		
		rootLog.setLevel(Level.DEBUG);
		
		//Turn down certain logging
		Logger jetty = (Logger) LoggerFactory.getLogger("org.eclipse.jetty");
		jetty.setLevel(Level.INFO);
	}
	
	public void startConsole(){
		ConsoleAppender<ILoggingEvent> c = new ConsoleAppender<ILoggingEvent>();
		c.setContext(rootContext);
		
		PatternLayoutEncoder ple = new PatternLayoutEncoder();
		ple.setContext(rootContext);
		ple.setPattern(pattern);
		ple.start();
		
		c.setEncoder(ple);
		c.start();
		
		rootLog.addAppender(c);
	}
}
