package net.lishaoy.perpro

import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import net.lishaoy.common.ui.PerBaseApplication
import net.lishaoy.library.log.PerConsolePrinter
import net.lishaoy.library.log.PerLogConfig
import net.lishaoy.library.log.PerLogConfig.JsonParser
import net.lishaoy.library.log.PerLogManager

@HiltAndroidApp
class MyApplication : PerBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        PerLogManager.init(object : PerLogConfig() {
            override fun getGlobalTag(): String {
                return super.getGlobalTag()
            }

            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }
        }, PerConsolePrinter())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}