package net.lishaoy.library.restful

interface PerCallback<T> {
    fun onSuccess(response: PerResponse<T>)
    fun onFailed(throwable: Throwable){}
}