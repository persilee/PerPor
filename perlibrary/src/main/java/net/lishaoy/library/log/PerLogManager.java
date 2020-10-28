package net.lishaoy.library.log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerLogManager {
    private PerLogConfig config;
    private static PerLogManager instance;
    private List<PerLogPrinter> printers = new ArrayList<>();
    private PerLogManager(PerLogConfig config, PerLogPrinter[] printers){
        this.config = config;
        this.printers.addAll(Arrays.asList(printers));
    }

    public static PerLogManager getInstance(){
        return instance;
    }

    public static void init(@NonNull PerLogConfig config, PerLogPrinter... printers) {
        instance = new PerLogManager(config, printers);
    }

    public PerLogConfig getConfig() {
        return config;
    }

    public List<PerLogPrinter> getPrinters() {
        return printers;
    }

    public void addPrinter(PerLogPrinter printer){
        printers.add(printer);
    }

    public void removePrinter(PerLogPrinter printer){
        if (printers != null) printers.remove(printer);
    }
}
