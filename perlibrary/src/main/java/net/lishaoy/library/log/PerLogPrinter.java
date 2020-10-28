package net.lishaoy.library.log;

import androidx.annotation.NonNull;

public interface PerLogPrinter {
    void print(@NonNull PerLogConfig config, int level, String tag, @NonNull String printString);
}
