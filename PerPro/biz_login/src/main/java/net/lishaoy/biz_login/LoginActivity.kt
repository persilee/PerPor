package net.lishaoy.biz_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_login.*
import net.lishaoy.biz_login.api.AccountApi
import net.lishaoy.common.flutter.PerFlutterBridge
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.util.PerStatusBar

@Route(path = "/account/login")
class LoginActivity : PerBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this,true, ContextCompat.getColor(baseContext,R.color.color_background),false)
        setContentView(R.layout.activity_login)

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
                        PerFlutterBridge.instance?.fire("onRefresh", null)
                        val data = response.data
                        AccountManager.loginSuccess(data!!)
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