package net.lishaoy.common.flutter.view

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.shim.ShimPluginRegistry
import io.flutter.plugin.common.PluginRegistry

object PerImageViewPlugin {

    fun registerWith(registrar: PluginRegistry.Registrar) {
        val viewFactory = PerImageViewFactory(registrar.messenger())
        registrar.platformViewRegistry().registerViewFactory("PerImageView", viewFactory)
    }

    fun registerWith(flutterEngine: FlutterEngine) {
        val shimPluginRegistry = ShimPluginRegistry(flutterEngine)
        registerWith(shimPluginRegistry.registrarFor("PerImageView"))
    }

}