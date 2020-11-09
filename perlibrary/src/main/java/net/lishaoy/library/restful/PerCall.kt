package net.lishaoy.library.restful

import java.io.IOException

interface PerCall<T> {

    @Throws(IOException::class)
    fun execute(): PerResponse<T>

    fun enqueue(callback: PerCallback<T>)

    interface Factory {

        fun newCall(request: PerRequest): PerCall<*>

    }

}