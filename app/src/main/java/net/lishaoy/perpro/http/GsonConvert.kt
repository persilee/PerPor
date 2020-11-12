package net.lishaoy.perpro.http

import net.lishaoy.library.restful.PerConvert
import net.lishaoy.library.restful.PerResponse
import java.lang.reflect.Type

class GsonConvert: PerConvert {
    override fun <T> convert(rawData: String, dataType: Type): PerResponse<T> {
        TODO("Not yet implemented")
    }
}