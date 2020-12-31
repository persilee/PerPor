package net.lishaoy.common.flutter

import android.os.Bundle
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import net.lishaoy.common.route.PerRoute
import net.lishaoy.library.util.AppGlobals

class PerFlutterBridge : IPerBridge<Any?, MethodChannel.Result>, MethodChannel.MethodCallHandler {

    private var methodChannels = mutableListOf<MethodChannel>()

    companion object {
        @JvmStatic
        var instance: PerFlutterBridge? = null
            private set

        @JvmStatic
        fun init(flutterEngine: FlutterEngine): PerFlutterBridge? {
            val methodChannel = MethodChannel(flutterEngine.dartExecutor, "PerFlutterBridge")
            if (instance == null) {
                PerFlutterBridge().also {
                    instance = it
                }
            }
            methodChannel.setMethodCallHandler(instance)
            instance!!.apply {
                methodChannels.add(methodChannel)
            }
            return instance
        }
    }

    fun fire(method: String, arguments: Any?) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments)
        }
    }

    fun fire(method: String, arguments: Any?, callback: MethodChannel.Result) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments, callback)
        }
    }

    override fun onBack(p: Any?) {

    }

    override fun goToNative(p: Any?) {
        if (p is Map<*,*>) {
            val action = p["action"]
            if (action == "goToDetail") {
                val goodsId = p["goodsId"]
                val bundle = Bundle()
                bundle.putString("goodsId", goodsId as String)
                PerRoute.startActivity(AppGlobals.get(), bundle, PerRoute.Destination.DETAIL_MAIN)
            }
        }

    }

    override fun getHeaderParams(callback: MethodChannel.Result) {

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when(call.method) {
            "onBack" -> onBack(call.arguments)
            "getHeaderParams" -> getHeaderParams(result)
            "goToNative" -> goToNative(call.arguments)
            else -> result.notImplemented()
        }
    }
}