package poi.tomtom;

import org.apache.log4j.Logger;

/**
 * Instead of log4j logger.
 * 
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 *
 */
public class LogCategory {

	@SuppressWarnings("unused")
	private static Logger root;
	private Logger logger;

	static {
		root = org.apache.log4j.Logger.getRootLogger();
	}

	public static LogCategory getLogger(String name) {
		return new LogCategory(Logger.getLogger(name));
	}

	public static LogCategory getLogger(Class<?> clazz) {
		return new LogCategory(Logger.getLogger(clazz));
	}

	protected LogCategory(Logger logger) {
		this.logger = logger;
	}

	public void trace(Object message) {
		logger.trace(message);
	}

	public void debug(Object message) {
		logger.debug(message);
	}

	public void info(Object message) {
		logger.info(message);
	}

	public void warn(Object message) {
		logger.warn(message);
	}

	public void error(Object message) {
		logger.error(message);
	}

	public void fatal(Object message) {
		logger.fatal(message);
	}
}
