package net.lishaoy.library.restful

interface PerInterceptor {

    fun intercept(chain: Chain): Boolean

    interface Chain {
        val isRequestPeriod: Boolean get() = false
        fun request(): PerRequest
        fun response(): PerResponse<*>?
    }

}