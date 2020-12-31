package net.lishaoy.common.ui

import android.app.Application
import net.lishaoy.common.flutter.PerFlutterCacheManager

open class PerBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PerFlutterCacheManager.instance?.preLoad(this)
    }
}