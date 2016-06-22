package logging;

/** Logging class, at the moment only used for more detailed print statements */
public class Logger {

  public static boolean DEBUG_OUTPUT_CALL_INFO = true;

  /**
   * Adds the call information to the current message (Where called from (Class, Method, Linenumber)
   * 
   * @param message
   * @return
   */
  private static final String concatCallInfo(String message) {
    return String.format("%-60s : %s", getCallInfo(5), message);
  }

  /**
   * 
   * @param depth
   * @return String representing call information from the stack at a specified depth
   */
  public static String getCallInfo(int depth) {
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

    if (depth >= stackTraceElements.length) {
      return "Requested depth outside scope";
    }

    String callInfo = null;

    StackTraceElement previousMethod = stackTraceElements[depth];
    System.out.println(previousMethod);

    if (previousMethod == null) {
      return "Strange call, cannot find caller";
    }

    String fileName = previousMethod.getFileName();
    String methodName = previousMethod.getMethodName();
    int lineNumber = previousMethod.getLineNumber();

    fileName = fileName == null ? "unknown" : fileName;
    methodName = methodName == null ? "unknown" : methodName;

    callInfo = String.format("%s::%s:%d", fileName.replace(".java", ""), methodName, lineNumber);
    return callInfo;
  }

  /**
   * Outputs the given message to the console, followed by a new line
   * 
   * @param output
   * @param message
   */
  public static final void println(String message) {

    if (DEBUG_OUTPUT_CALL_INFO)
      message = concatCallInfo(message);

    System.out.println(message);
  }

}