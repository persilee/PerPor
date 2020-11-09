package net.lishaoy.library.restful

import androidx.annotation.IntDef
import java.lang.reflect.Type

open class PerRequest {

    @METHOD
    var httpMethod: Int = 0
    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, Any>? = null
    var domainUrl: String? = null
    var relativeUrl: String? = null
    var returnType: Type? = null

    @IntDef(value = [METHOD.GET, METHOD.POST])
    internal annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }

}
