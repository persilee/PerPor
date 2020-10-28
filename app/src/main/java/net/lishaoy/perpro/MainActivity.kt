package net.lishaoy.perpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.lishaoy.perpro.demo.PerLogDemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.log -> {
                startActivity(Intent(this, PerLogDemoActivity::class.java))
            }
        }
    }
}