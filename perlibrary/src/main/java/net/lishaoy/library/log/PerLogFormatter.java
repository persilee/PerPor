package net.lishaoy.library.log;

public interface PerLogFormatter<T> {
    String format(T data);
}
