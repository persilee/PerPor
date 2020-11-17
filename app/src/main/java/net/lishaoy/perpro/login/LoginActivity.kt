package net.lishaoy.perpro.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.perpro.R

class LoginActivity : PerBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        btn_back.setOnClickListener{
            onBackPressed()
        }

        btn_register.setOnClickListener {

        }
    }
}