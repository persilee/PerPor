package net.lishaoy.library.log;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class PerLog {

    private static final String PER_LOG_PACKAGE;

    static {
        String className = PerLog.class.getName();
        PER_LOG_PACKAGE = className.substring(0, className.lastIndexOf('.') + 1);
    }

    public static void v(Object... contents) {
        log(PerLogType.V, contents);
    }

    public static void vt(String tag, Object... contents) {
        log(PerLogType.V, tag, contents);
    }

    public static void d(Object... contents) {
        log(PerLogType.D, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(PerLogType.D, tag, contents);
    }

    public static void i(Object... contents) {
        log(PerLogType.I, contents);
    }

    public static void it(String tag, Object... contents) {
        log(PerLogType.I, tag, contents);
    }

    public static void w(Object... contents) {
        log(PerLogType.W, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(PerLogType.W, tag, contents);
    }

    public static void e(Object... contents) {
        log(PerLogType.E, contents);
    }

    public static void et(String tag, Object... contents) {
        log(PerLogType.E, tag, contents);
    }

    public static void a(Object... contents) {
        log(PerLogType.A, contents);
    }

    public static void at(String tag, Object... contents) {
        log(PerLogType.A, tag, contents);
    }

    public static void log(@PerLogType.TYPE int type, Object... contents) {
        log(type, PerLogManager.getInstance().getConfig().getGlobalTag(), contents);
    }

    public static void log(@PerLogType.TYPE int type, @NonNull String tag, Object... contents) {
        log(PerLogManager.getInstance().getConfig(), type, tag, contents);
    }

    public static void log(@NonNull PerLogConfig config, @PerLogType.TYPE int type, @NonNull String tag, Object... contents) {
        if (!config.enable()) return;
        StringBuffer sb = new StringBuffer();
        if (config.includeTread()) {
            String threadInfo = PerLogConfig.PER_THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth() > 0) {
            String stackTrace = PerLogConfig.PER_STACKTRACE_FORMATTER.format(PerStackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(), PER_LOG_PACKAGE, config.stackTraceDepth()));
            sb.append(stackTrace).append("\n");
        }
        String body = parseBody(contents, config);
        if (body != null) {//替换转义字符\
            body = body.replace("\\\"", "\"");
        }
        sb.append(body);
        List<PerLogPrinter> printers = config.printers() != null ? Arrays.asList(config.printers()) : PerLogManager.getInstance().getPrinters();
        if (printers == null) return;
        for (PerLogPrinter printer : printers) {
            printer.print(config, type, tag, sb.toString());
        }
    }

    private static String parseBody(@NonNull Object[] contents, @NonNull PerLogConfig config) {
        if (config.injectJsonParser() != null) {
            if (contents.length == 1 && contents[0] instanceof String) {
                return (String) contents[0];
            }

            return config.injectJsonParser().toJson(contents);
        }
        StringBuffer sb = new StringBuffer();
        for (Object o : contents) {
            sb.append(o.toString()).append(";");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
