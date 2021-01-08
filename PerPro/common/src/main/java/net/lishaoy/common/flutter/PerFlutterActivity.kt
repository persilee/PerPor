package net.lishaoy.common.flutter

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_per_flutter.*
import net.lishaoy.common.R
import net.lishaoy.common.route.PerRoute
import net.lishaoy.library.util.PerStatusBar

@Route(path = "/flutter/main")
class PerFlutterActivity : AppCompatActivity() {

    @JvmField
    @Autowired
    var module: String? = null

    @JvmField
    @Autowired
    var title: String? = null

    var flutterFragment: MyFlutterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerRoute.inject(this)
        PerStatusBar.setStatusBar(this, true, statusBarColor = title?.let { Color.WHITE } ?: Color.TRANSPARENT, translucent = title?.let { false } ?: true)
        setContentView(R.layout.activity_per_flutter)
        initUI()
        initFragment()
    }

    private fun initUI() {
        title?.let {
            nav_bar.setTitle(it)
            nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
            nav_bar.visibility = View.VISIBLE
        }
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