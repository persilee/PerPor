package net.lishaoy.perpro.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_per_log_demo.*
import net.lishaoy.library.log.PerLog
import net.lishaoy.perpro.R

class PerLogDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_per_log_demo)
        btn_log.setOnClickListener {
            printLog()
        }
    }

    private fun printLog(){
        PerLog.a("6666")
    }
}