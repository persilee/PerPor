package net.lishaoy.common.http

import net.lishaoy.library.restful.PerRestful

object ApiFactory {
    private val baseUrl = "https://api.devio.org/as/"
    private val perRestful: PerRestful = PerRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        perRestful.addInterceptor(BizInterceptor())
        perRestful.addInterceptor(HttpStatusInterceptor())
    }

    fun <T> create(service: Class<T>): T {
        return perRestful.create(service)
    }
}