package net.lishaoy.perpro.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import androidx.core.view.get
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_login.*
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.util.SPUtil
import net.lishaoy.perpro.R
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.AccountApi

@Route(path = "/account/login")
class LoginActivity : PerBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        btn_back.setOnClickListener{
            onBackPressed()
        }

        btn_register.setOnClickListener {
            goRegistration()
        }

        btn_login.setOnClickListener {
            goLogin()
        }


    }

    private fun goLogin() {
        val name = input_item_username.getEditText().text
        val password = input_item_password.getEditText().text

        if (TextUtils.isEmpty(name) or TextUtils.isEmpty(password)) {
            return
        }

        ApiFactory.create(AccountApi::class.java).login(name.toString(), password.toString())
            .enqueue(object : PerCallback<String> {
                override fun onSuccess(response: PerResponse<String>) {
                    if (response.code == PerResponse.SUCCESS) {
                        showToast(getString(R.string.login_success))
                        val data = response.data
                        SPUtil.putString("boarding-pass", data!!)
                        setResult(Activity.RESULT_OK, Intent())
                        finish()
                    } else {
                        showToast(getString(R.string.login_fail) + response.msg)
                    }

                }

                override fun onFailed(throwable: Throwable) {
                    showToast(getString(R.string.login_fail) + throwable.message)
                }

            })
    }

    private fun goRegistration() {
//        startActivity(Intent(this, RegistrationActivity::class.java))
        ARouter.getInstance().build("/account/register").navigation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data != null)) {
            val username = data!!.getStringExtra("username")
            input_item_username.getEditText().setText(username)
        }
    }
}