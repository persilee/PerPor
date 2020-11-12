package net.lishaoy.perpro.http

import net.lishaoy.library.restful.PerRestful

object ApiFactory {
    private const val baseUrl = "https://api.devio.org/as/"
    private val perRestful: PerRestful = PerRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        perRestful.addInterceptor(BizInterceptor())
    }

    fun <T> create(service: Class<T>): T {
        return perRestful.create(service)
    }
}