package net.lishaoy.perpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.lishaoy.perpro.demo.PerBottmLayoutDemoActivity
import net.lishaoy.perpro.demo.PerLogDemoActivity
import net.lishaoy.ui.tab.bottom.PerTabBottomInfo

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.log -> {
                startActivity(Intent(this, PerLogDemoActivity::class.java))
            }
            R.id.tab_layout -> {
                startActivity(Intent(this, PerBottmLayoutDemoActivity::class.java))
            }
        }
    }
}