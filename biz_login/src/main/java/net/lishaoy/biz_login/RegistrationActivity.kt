package net.lishaoy.biz_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_registration.*
import net.lishaoy.biz_login.api.AccountApi
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.util.PerRes
import net.lishaoy.library.util.PerStatusBar

@Route(path = "/account/register")
class RegistrationActivity : PerBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this,true, ContextCompat.getColor(baseContext,R.color.color_background),false)
        setContentView(R.layout.activity_registration)

        nav_bar.setTitle(PerRes.getSting(R.string.register))
        nav_bar.setNavListener(View.OnClickListener {
            onBackPressed()
        })

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