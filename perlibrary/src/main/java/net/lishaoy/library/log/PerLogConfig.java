package net.lishaoy.library.log;

public abstract class PerLogConfig {

    public String getGlobalTag(){
        return "PerLog";
    }

    public boolean enable(){
        return true;
    }

}
