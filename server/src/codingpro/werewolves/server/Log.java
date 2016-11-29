package codingpro.werewolves.server;

public class Log {
	
	private Log() {}
	
	public static final boolean debugEnabled = false;
	private static final Object printLock = new Object();
	
	public static void response(String text) {
		synchronized (printLock) {
			System.out.println(text);
		}
	}
	
	public static void info(String text) {
		synchronized (printLock) {
			System.out.println("INFO | " + text);
		}
	}
	
	public static void warning(String text) {
		synchronized (printLock) {
			System.out.println("WARNING | " + text);
		}
	}
	
	public static void error(String text) {
		synchronized (printLock) {
			System.out.println("ERROR | " + text);
		}
	}
	
	public static void debug(String text) {
		if (debugEnabled) {
			synchronized (printLock) {
				System.out.println("DEBUG | " + text);
			}
		}
	}
	
}
