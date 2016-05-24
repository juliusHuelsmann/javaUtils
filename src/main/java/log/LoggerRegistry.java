package log;

import java.util.Vector;

public class LoggerRegistry {

  
  /**
   * The only instance of this class.
   */
  private static LoggerRegistry instance;
  
  
  /**
   * Vector of all loggers.
   */
  private final Vector<Logger> vecLog;
  
  
  /**
   * Constructor: initializes the Logger-Vector.
   */
  public LoggerRegistry() {
    vecLog = new Vector<Logger>();
  }
  
  
  
  /**
   * Return the only {@link #instance} of this class.
   */
  private static LoggerRegistry getInstance() {
    if (instance == null) {
      instance = new LoggerRegistry();
    }
    return instance;
  }
  
  
  /**
   * Register new logger.
   * @param xlog  the new logger.
   */
  public static final void registerLogger(final Logger xlog) {
    
    if (!isRegistered(xlog)) {
      getInstance().vecLog.addElement(xlog);
    }
  }
  
  
  /**
   * Remove logger and return whether the removal has been successful.
   * @param xlog    the Logger
   * @return        whether the logger has been found and removed.
   */
  public static final boolean removeLogger(final Logger xlog) {
    
    if (isRegistered(xlog)) {
      getInstance().vecLog.remove(xlog);
      return true;
    }
    
    return false;
  }
  
  
  
  /**
   * Check whether a Logger has already been added. 
   * @param xlog    the logger,
   * @return        whether the logger has already been added.
   */
  private static final boolean isRegistered(final Logger xlog) {
    for (Logger d : getInstance().vecLog) {
      if (d.equals(xlog)) {
        return true;
      }
    }
    return false;
  }
  
  
  /**
   * Log message at all registered loggers.
   * @param xlog  the message.
   */
  public static final void log(final String xlog) {
    for (Logger d : getInstance().vecLog) {
      d.log(xlog);
    }
  }
}
