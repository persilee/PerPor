package net.lishaoy.library.util

import android.os.Handler
import android.os.Looper
import android.os.Message


object MainHandler {

    private val handler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable) {
        handler.post(runnable)
    }

    fun remove(runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }

    fun postDelay(delayMillis: Long, runnable: Runnable) {
        handler.postDelayed(runnable, delayMillis)
    }

    fun sendAtFromOfQueue(runnable: Runnable) {
        handler.sendMessageAtFrontOfQueue(Message.obtain(handler, runnable))
    }

}