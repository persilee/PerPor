package net.lishaoy.library.restful

import androidx.annotation.IntDef
import org.devio.hi.library.restful.annotation.DELETE
import org.devio.hi.library.restful.annotation.PUT
import java.lang.IllegalStateException
import java.lang.reflect.Type

open class PerRequest {

    @METHOD
    var httpMethod: Int = 0
    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, String>? = null
    var domainUrl: String? = null
    var relativeUrl: String? = null
    var returnType: Type? = null
    var formPost: Boolean = true

    @IntDef(value = [METHOD.GET, METHOD.POST, METHOD.PUT, METHOD.DELETE])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
            const val PUT = 2
            const val DELETE = 3
        }
    }

    fun endPointUrl(): String {
        if (relativeUrl == null) {
            throw IllegalStateException("relative url must bot be null")
        }
        if (!relativeUrl!!.startsWith("/")) {
            return domainUrl + relativeUrl
        }
        val indexOf = domainUrl!!.indexOf("/")
        return domainUrl!!.substring(0, indexOf) + relativeUrl
    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![name] = value
    }

}