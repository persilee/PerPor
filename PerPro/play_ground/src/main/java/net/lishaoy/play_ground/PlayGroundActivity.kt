package net.lishaoy.play_ground

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_play_ground.*
import net.lishaoy.library.util.PerStatusBar

@Route(path = "/playground/main")
class PlayGroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this, true, translucent = false)
        setContentView(R.layout.activity_play_ground)
        initUI()
    }

    private fun initUI() {

        nav_bar.setTitle("游乐场")
        nav_bar.setNavListener(View.OnClickListener {
            onBackPressed()
        })
        btn_to_flutter.setOnClickListener {
            ARouter.getInstance().build("/flutter/main")
                .withString("module", "nativeView").navigation()
        }

    }
}