package net.lishaoy.perpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.lishaoy.perpro.demo.PerLogDemoActivity
import net.lishaoy.ui.tab.bottom.PerTabBottomInfo

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tab_bottom.setPerTabInfo(PerTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        ))
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.log -> {
                startActivity(Intent(this, PerLogDemoActivity::class.java))
            }
        }
    }
}