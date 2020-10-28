package net.lishaoy.library.log;

import androidx.annotation.NonNull;

public class PerLogManager {
    private PerLogConfig config;
    private static PerLogManager instance;
    private PerLogManager(PerLogConfig config){
        this.config = config;
    }

    public static PerLogManager getInstance(){
        return instance;
    }

    public static void init(@NonNull PerLogConfig config) {
        instance = new PerLogManager(config);
    }

    public PerLogConfig getConfig() {
        return config;
    }
}
