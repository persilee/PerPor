package net.lishaoy.library.restful

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap


open class PerRestful(val baseUrl: String, callFactory: PerCall.Factory) {

    private var interceptors: MutableList<PerInterceptor> = mutableListOf()
    private var methodService: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap()
    private var scheduler: Scheduler

    init {
         scheduler = Scheduler(callFactory, interceptors)
    }

    fun addInterceptor(interceptor: PerInterceptor) {
        interceptors.add(interceptor)
    }

    fun <T> create(service: Class<T>) : T {
        return Proxy.newProxyInstance(service.classLoader, arrayOf<Class<*>>(service)) { proxy, method, args ->
            var methodParser = methodService[method]
            if (methodParser == null) {
                methodParser = MethodParser.parse(baseUrl, method, args)
                methodService[method] = methodParser
            }
            val request = methodParser.newRequest(method, args)

            scheduler.newCall(request)
        } as T
    }

}