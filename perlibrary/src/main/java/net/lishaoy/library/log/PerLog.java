package net.lishaoy.library.log;

import android.util.Log;

import androidx.annotation.NonNull;

public class PerLog {

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
        String body = parseBody(contents);
        sb.append(body);
        Log.println(type, tag, body);
    }

    private static String parseBody(@NonNull Object[] contents) {
        StringBuffer sb = new StringBuffer();
        for (Object o : contents) {
            sb.append(o.toString()).append(";");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
