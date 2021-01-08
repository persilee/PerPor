package net.lishaoy.common.flutter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.fragment_flutter.*
import net.lishaoy.common.R
import net.lishaoy.common.ui.PerBaseFragment
import net.lishaoy.library.util.AppGlobals

abstract class PerFlutterFragment(module: String) : PerBaseFragment() {

    private var flutterEngine: FlutterEngine? = null
    private var flutterView: FlutterView? = null
    private val cached = PerFlutterCacheManager.instance!!.hasCached(module)

    init {
        flutterEngine =
            PerFlutterCacheManager.instance!!.getCachedFlutterEngine(AppGlobals.get(), module)
    }

    private fun createFlutterView(context: Context): FlutterView {
        val flutterTextureView = FlutterTextureView(activity!!)
        flutterView = FlutterView(context, flutterTextureView)
        return flutterView!!
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_flutter
    }

    fun setTitle(title: String) {
        nav_bar.visibility = View.VISIBLE
        nav_bar.setTitle(title)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (layoutView as ViewGroup).addView(createFlutterView(activity!!))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!cached) {
            flutterEngine?.platformViewsController?.attach(
                activity,
                flutterEngine!!.renderer,
                flutterEngine!!.dartExecutor
            )
        }
    }

    override fun onStart() {
        flutterView!!.attachToFlutterEngine(flutterEngine!!)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        flutterEngine!!.lifecycleChannel.appIsResumed()
    }

    override fun onPause() {
        super.onPause()
        flutterEngine!!.lifecycleChannel.appIsInactive()
    }

    override fun onStop() {
        super.onStop()
        flutterEngine!!.lifecycleChannel.appIsPaused()
    }

    override fun onDetach() {
        super.onDetach()
        flutterEngine!!.lifecycleChannel.appIsDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterView?.detachFromFlutterEngine()
    }
}