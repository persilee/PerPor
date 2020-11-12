package net.lishaoy.library.restful

import org.devio.hi.library.restful.annotation.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MethodParser(private val baseUrl: String, method: Method, args: Array<Any>) {

    private var domainUrl: String? = null
    private var formPost: Boolean = true
    private var httpMethod: Int = 0
    private lateinit var relativeUrl: String
    private var replaceRelativeUrl: String? = null
    private lateinit var type: Type
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var parameters: MutableMap<String, String> = mutableMapOf()

    companion object {
        fun parse(baseUrl: String, method: Method, args: Array<Any>): MethodParser {
            return MethodParser(baseUrl, method, args)
        }
    }

    init {
        parseMethodAnnotations(method)
        parseMethodParameters(method, args)
        parseMethodReturnType(method)
    }

    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != PerCall::class.java) {
            throw IllegalStateException("method ${method.name} must be type of PerCall.class")
        }

        val genericReturnType = method.genericReturnType // 获取泛型参数
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) { "method ${method.name} can only has generic return type" }
            type = actualTypeArguments[0]
        } else {
            throw IllegalStateException("method ${method.name} must be one generic return type")
        }
    }


    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        val pas = method.parameterAnnotations
        val equals = pas.size == args.size
        require(equals) { "arguments annotations count ${pas.size} don`t match count ${args.size}" }
        for (index in args.indices) {
            val annotations = pas[index]
            require(annotations.size <= 1) { "filed can only has one annotation: index = $index" }
            val value = args[index]
            require(isPrimitive(value)) { "8 basic types are supported for now, index = $index" }
            val annotation = annotations[0]
            if (annotation is Filed) {
                val key = annotation.value
                val value = args[index]
                parameters[key] = value.toString()
            } else if (annotation is Path) {
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    val newRelativeUrl = relativeUrl.replace("{$replaceName}", replacement)
                    relativeUrl = newRelativeUrl
                }
            } else {
                throw IllegalStateException("cannot handle parameter annotation: ${annotation.javaClass.toString()}")
            }
        }
    }

    private fun isPrimitive(value: Any): Boolean {
        if (value.javaClass == String::class.java) {
            return true
        }
        try {
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun parseMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            if (annotation is GET) {
                relativeUrl = annotation.value
                httpMethod = PerRequest.METHOD.GET
            } else if (annotation is POST) {
                relativeUrl = annotation.value
                httpMethod = PerRequest.METHOD.POST
                formPost = annotation.formPost
            } else if (annotation is Headers) {
                val headerArray = annotation.value
                for (header in headerArray) {
                    val indexOf = header.indexOf(":")
                    check(!(indexOf == 0 || indexOf == -1)) {
                        "@headers value must be in the form [name: value], but found [${header}]"
                    }
                    val name = header.substring(0, indexOf)
                    val value = header.substring(indexOf + 1).trim()
                    headers[name] = value
                }
            } else if (annotation is BaseUrl) {
                domainUrl = annotation.value
            } else {
                throw IllegalStateException("cannot handle method annotation: ${annotation.javaClass.toString()}")
            }
        }

        require(
            (httpMethod == PerRequest.METHOD.GET)
                    || (httpMethod == PerRequest.METHOD.POST
                    || (httpMethod == PerRequest.METHOD.PUT)
                    || (httpMethod == PerRequest.METHOD.DELETE))
        ) {
            String.format("method %s must has one of GET,POST,PUT,DELETE ", method.name)
        }

        if (domainUrl == null) {
            domainUrl = baseUrl
        }
    }

    fun newRequest(method: Method, args: Array<out Any>?): PerRequest {
        val arguments: Array<Any> = args as Array<Any>? ?: arrayOf()
        parseMethodParameters(method, arguments)

        val request = PerRequest()
        request.domainUrl = domainUrl
        request.returnType = type
        request.relativeUrl = replaceRelativeUrl ?: relativeUrl
        request.parameters = parameters
        request.headers = headers
        request.httpMethod = httpMethod
        request.formPost = formPost
        return request
    }

}