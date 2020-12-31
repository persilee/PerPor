package net.lishaoy.common.http

import net.lishaoy.library.log.PerLog
import net.lishaoy.library.restful.PerInterceptor
import net.lishaoy.service_login.LoginServiceProvider


class BizInterceptor : PerInterceptor {
    override fun intercept(chain: PerInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            request.addHeader("auth-token", "MTU5Mjg1MDg3NDcwNw11.26==")
            request.addHeader("boarding-pass", LoginServiceProvider.getBoardingPass() ?: "")
        } else if (chain.response() != null) {
            PerLog.dt("BizInterceptor", chain.request().endPointUrl())
            PerLog.dt("BizInterceptor", chain.response()!!.rawData)
        }

        return false
    }
}