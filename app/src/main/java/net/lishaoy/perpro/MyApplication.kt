package net.lishaoy.perpro

import android.app.Application
import com.google.gson.Gson
import net.lishaoy.library.log.PerConsolePrinter
import net.lishaoy.library.log.PerLogConfig
import net.lishaoy.library.log.PerLogManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PerLogManager.init(object :PerLogConfig(){
            override fun getGlobalTag(): String {
                return super.getGlobalTag()
            }

            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }
        }, PerConsolePrinter())
    }
}