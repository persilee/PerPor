package net.lishaoy.library.restful

import android.text.TextUtils
import androidx.annotation.IntDef
import net.lishaoy.library.restful.annotation.CacheStrategy
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.reflect.Type
import java.net.URLEncoder

open class PerRequest {

    private var cacheStrategyKey: String = ""

    @METHOD
    var httpMethod: Int = 0
    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, String>? = null
    var domainUrl: String? = null
    var relativeUrl: String? = null
    var returnType: Type? = null
    var formPost: Boolean = true
    var cacheStrategy: Int = CacheStrategy.NET_ONLY

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

    fun getCacheKey(): String {
        if (!TextUtils.isEmpty(cacheStrategyKey)) {
            return cacheStrategyKey
        }
        val builder = StringBuffer()
        val endUrl = endPointUrl()
        builder.append(endUrl)
        if (endUrl.indexOf("?") > 0 || endUrl.indexOf("&") > 0) {
            builder.append("&")
        } else {
            builder.append("?")
        }

        if (parameters != null) {
            for ((key, value) in parameters!!) {
                try {
                    val encodeValue = URLEncoder.encode(value, "UTF-8")
                    builder.append(key).append("-").append(encodeValue).append("&")
                } catch (e: Exception) {

                }
            }
            builder.deleteCharAt(builder.length - 1)
            cacheStrategyKey = builder.toString()
        } else {
            cacheStrategyKey = endUrl
        }

        return cacheStrategyKey

    }

}
