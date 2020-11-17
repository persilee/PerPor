package net.lishaoy.perpro.http

import net.lishaoy.library.log.PerLog
import net.lishaoy.library.restful.PerInterceptor
import net.lishaoy.library.util.SPUtil

class BizInterceptor : PerInterceptor {
    override fun intercept(chain: PerInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            val boardingPass = SPUtil.getString("boarding-pass") ?: ""
            request.addHeader("boarding-pass", boardingPass)
            request.addHeader("auth-token", "MTU5Mjg1MDg3NDcwNw==")
        } else if (chain.response() != null) {
            PerLog.dt("BizInterceptor", chain.request().endPointUrl())
            PerLog.dt("BizInterceptor", chain.response()!!.rawData)
        }

        return false
    }
}