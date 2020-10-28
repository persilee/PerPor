package net.lishaoy.library.log;

public class PerThreadFormatter implements PerLogFormatter<Thread> {
    @Override
    public String format(Thread data) {
        return data.getName();
    }
}
