package net.lishaoy.library.log;

public abstract class PerLogConfig {

    static int MAX_LEN = 512;
    static PerThreadFormatter PER_THREAD_FORMATTER = new PerThreadFormatter();
    static PerStackTraceFormatter PER_STACKTRACE_FORMATTER = new PerStackTraceFormatter();

    public JsonParser injectJsonParser() {
        return null;
    }

    public String getGlobalTag() {
        return "PerLog";
    }

    public boolean enable() {
        return true;
    }

    public boolean includeTread() {
        return false;
    }

    public int stackTraceDepth() {
        return 6;
    }

    public PerLogPrinter[] printers() {
        return null;
    }

    public interface JsonParser {
        String toJson(Object src);
    }

}
