package net.lishaoy.perpro

import android.app.Application
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
                return super.injectJsonParser()
            }
        })
    }
}