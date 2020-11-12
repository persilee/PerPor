package net.lishaoy.library.restful

import java.lang.reflect.Type

interface PerConvert {
    fun <T> convert(rawData: String, dataType: Type): PerResponse<T>
}