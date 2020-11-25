package net.lishaoy.library.restful

import net.lishaoy.library.cache.PerStorage
import net.lishaoy.library.executor.PerExecutor
import net.lishaoy.library.util.MainHandler
import org.devio.hi.library.restful.annotation.CacheStrategy

class Scheduler(
    private val callFactory: PerCall.Factory,
    val interceptors: MutableList<PerInterceptor>
) {
    fun newCall(request: PerRequest): PerCall<*> {
        val newCall = callFactory.newCall(request)
        return ProxyCall(newCall, request)
    }

    internal inner class ProxyCall<T>(
        private val delegate: PerCall<T>,
        val request: PerRequest
    ) : PerCall<T> {
        override fun execute(): PerResponse<T> {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                val cacheResponse = readCache<T>()
                if (cacheResponse.data != null) {
                    return cacheResponse
                }
            }
            val response = delegate.execute()
            saveCacheIfNeed(response)
            dispatchInterceptor(request, response)
            return response
        }

        private fun dispatchInterceptor(request: PerRequest, response: PerResponse<T>?) {
            if (interceptors.size < 0) return
            InterceptorChain(request, response).dispatch()
        }

        override fun enqueue(callback: PerCallback<T>) {
            dispatchInterceptor(request, null)

            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                PerExecutor.execute(runnable = Runnable {
                    val cacheResponse = readCache<T>()
                    if (cacheResponse.data != null) {
                        MainHandler.sendAtFromOfQueue(runnable = Runnable {
                            callback.onSuccess(cacheResponse)
                        })
                    }
                })
            }

            delegate.enqueue(object : PerCallback<T> {
                override fun onSuccess(response: PerResponse<T>) {
                    dispatchInterceptor(request, response)
                    saveCacheIfNeed(response)
                    callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callback.onFailed(throwable)
                }

            })
        }

        private fun saveCacheIfNeed(response: PerResponse<T>) {
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST || request.cacheStrategy == CacheStrategy.NET_CACHE) {
                if (response.data != null) {
                    PerExecutor.execute(runnable = Runnable {
                        PerStorage.saveCache(request.getCacheKey(), response.data)
                    })
                }
            }
        }

        private fun <T> readCache(): PerResponse<T> {
            val cacheKey = request.getCacheKey()
            val cache = PerStorage.getCache<T>(cacheKey)
            val cacheResponse = PerResponse<T>()
            cacheResponse.data = cache
            cacheResponse.code = PerResponse.CACHE_SUCCESS
            cacheResponse.msg = "get cache success"
            return cacheResponse
        }

        internal inner class InterceptorChain(
            val request: PerRequest,
            val response: PerResponse<T>?
        ) : PerInterceptor.Chain {
            var callIndex: Int = 0
            override fun request(): PerRequest {
                return request
            }

            override fun response(): PerResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }

            override val isRequestPeriod: Boolean
                get() = response == null

        }

    }

}