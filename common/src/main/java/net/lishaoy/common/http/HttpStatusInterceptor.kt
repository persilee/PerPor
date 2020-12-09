package net.lishaoy.common.http

import com.alibaba.android.arouter.launcher.ARouter
import net.lishaoy.library.restful.PerInterceptor
import net.lishaoy.library.restful.PerResponse

class HttpStatusInterceptor : PerInterceptor {
    override fun intercept(chain: PerInterceptor.Chain): Boolean {
        val response = chain.response()
        if (!chain.isRequestPeriod && response != null) {
            when (response.code) {
                PerResponse.RC_NEED_LOGIN -> {
                    ARouter.getInstance().build("/account/login").navigation()
                }
                PerResponse.RC_AUTH_TOKEN_EXPIRED, PerResponse.RC_AUTH_TOKEN_INVALID, PerResponse.RC_USER_FORBID -> {
                    ARouter.getInstance().build("/degrade/global/activity")
                        .withString("degrade_title", "非法访问")
                        .withString("degrade_desc", response.msg)
                        .withString("degrade_action", response.errorData?.get("helpUrl"))
                        .navigation()

                }
            }
        }

        return false
    }
}