package net.lishaoy.common.flutter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import net.lishaoy.common.R
import net.lishaoy.common.route.PerRoute
import net.lishaoy.library.util.PerStatusBar

@Route(path = "/flutter/main")
class PerFlutterActivity : AppCompatActivity() {

    @JvmField
    @Autowired
    var module: String? = null
    var flutterFragment: MyFlutterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerRoute.inject(this)
        PerStatusBar.setStatusBar(this, true, translucent = true)
        setContentView(R.layout.activity_per_flutter)
        initFragment()
    }

    private fun initFragment() {
        flutterFragment = MyFlutterFragment(module!!)
        supportFragmentManager.beginTransaction().add(R.id.flutter_view, flutterFragment!!).commit()
    }

    class MyFlutterFragment(private val module: String): PerFlutterFragment(module) {

        override fun onDestroy() {
            super.onDestroy()
            PerFlutterCacheManager.instance?.destroyCached(module)
        }

    }

}