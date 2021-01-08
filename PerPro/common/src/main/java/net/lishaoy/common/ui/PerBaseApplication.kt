package net.lishaoy.common.ui

import android.app.Application
import net.lishaoy.common.flutter.PerFlutterCacheManager
import net.lishaoy.library.util.PerActivityManager

open class PerBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PerActivityManager.instance.init(this)
        PerFlutterCacheManager.instance?.preLoad(this)
    }
}