package net.lishaoy.perpro.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_per_log_demo.*
import net.lishaoy.library.log.PerLog
import net.lishaoy.library.log.PerLogConfig
import net.lishaoy.library.log.PerLogType
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
        PerLog.log(object :PerLogConfig(){
            override fun includeTread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }
        }, PerLogType.D, TAG, "8888")
        PerLog.e("6666");
    }

    companion object {
        private const val TAG = "PerLogDemoActivity"
    }
}