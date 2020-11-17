package net.lishaoy.library.restful

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
            val response = delegate.execute()
            dispatchInterceptor(request, response)
            return response
        }

        private fun dispatchInterceptor(request: PerRequest, response: PerResponse<T>?) {
            if (interceptors.size < 0) return
            InterceptorChain(request, response).dispatch()
        }

        override fun enqueue(callback: PerCallback<T>) {
            dispatchInterceptor(request, null)

            delegate.enqueue(object :PerCallback<T>{
                override fun onSuccess(response: PerResponse<T>) {
                    dispatchInterceptor(request, response)
                    callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callback.onFailed(throwable)
                }

            })
        }

        internal inner class InterceptorChain(val request: PerRequest, val response: PerResponse<T>?): PerInterceptor.Chain {
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
                callIndex ++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }

            override val isRequestPeriod: Boolean
                get() = response == null

        }

    }

}