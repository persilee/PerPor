package net.lishaoy.perpro.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_registration.*
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.util.PerStatusBar
import net.lishaoy.perpro.R
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.AccountApi

@Route(path = "/account/register")
class RegistrationActivity : PerBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar?.hide()
        PerStatusBar.setStatusBar(this,true, ContextCompat.getColor(baseContext,R.color.color_background),false)

        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_submit.setOnClickListener {
            submit()
        }

    }

    private fun submit() {
        val orderId = input_item_order_id.getEditText().text.toString()
        val imoocId = input_item_imooc_id.getEditText().text.toString()
        val username = input_item_username.getEditText().text.toString()
        val password = input_item_password.getEditText().text.toString()

        if ((TextUtils.isEmpty(orderId)) or (TextUtils.isEmpty(imoocId)) or (TextUtils.isEmpty(username)) or (TextUtils.isEmpty(password))) {
            return
        }

        ApiFactory.create(AccountApi::class.java).register(username,password,orderId,imoocId)
            .enqueue(object :PerCallback<String> {
                override fun onSuccess(response: PerResponse<String>) {
                    if (response.code == PerResponse.SUCCESS) {
                        showToast("注册成功")
                        val intent = Intent()
                        intent.putExtra("username", username)
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        showToast("注册失败" + response.msg)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast("注册失败" + throwable.message)
                }

            })
    }
}