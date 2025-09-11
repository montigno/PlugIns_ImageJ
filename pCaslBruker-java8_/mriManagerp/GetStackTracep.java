package mriManagerp;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GetStackTracep {
	
	private static Throwable th;
	public static int numberOfError;
		
	public GetStackTracep(final Throwable th) {
		GetStackTracep.setTh(th);
	}
	
	public static String getMessage() {
		final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     getTh().printStackTrace(pw);
	     numberOfError++;
	     return "\n Error n° "+numberOfError + "\n"+sw.getBuffer().toString();
	}

	public static Throwable getTh() {
		return th;
	}

	public static void setTh(Throwable th) {
		GetStackTracep.th = th;
	}
}