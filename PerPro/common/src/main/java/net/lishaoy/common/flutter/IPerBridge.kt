package net.lishaoy.common.flutter

interface IPerBridge <P, Callback>{

    fun onBack(p: P)
    fun goToNative(p: P)
    fun getHeaderParams(callback: Callback)

}