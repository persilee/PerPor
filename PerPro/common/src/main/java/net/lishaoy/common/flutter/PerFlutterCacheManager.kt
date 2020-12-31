package net.lishaoy.common.flutter

import android.content.Context
import android.os.Looper
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.view.FlutterMain

class PerFlutterCacheManager private constructor(){

    companion object {
        const val MODULE_FAVORITE = "main"
        const val MODULE_RECOMMEND = "recommend"

        @JvmStatic
        @get:Synchronized
        var instance: PerFlutterCacheManager? = null
        get() {
            if (field == null) {
                field = PerFlutterCacheManager()
            }
            return field
        }
        private set
    }

    private fun initFlutterEngine(context: Context, module: String): FlutterEngine {
        val flutterEngine = FlutterEngine(context)
        PerFlutterBridge.init(flutterEngine)
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint(FlutterMain.findAppBundlePath(), module))
        FlutterEngineCache.getInstance().put(module, flutterEngine)

        return flutterEngine
    }

    fun preLoad(context: Context) {
        Looper.myQueue().addIdleHandler {
            initFlutterEngine(context, MODULE_FAVORITE)
            initFlutterEngine(context, MODULE_RECOMMEND)
            false
        }
    }

    fun getCachedFlutterEngine(context: Context?, module: String): FlutterEngine {
        var flutterEngine = FlutterEngineCache.getInstance()[module]
        if (flutterEngine == null && context != null) {
            flutterEngine = initFlutterEngine(context, module)
        }

        return flutterEngine!!
    }

}