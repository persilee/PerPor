package net.lishaoy.library.util

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

object PerDataBus {

    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()

    fun <T> with(eventName: String): StickyLiveData<T> {
        var liveData = eventMap[eventName]
        if (liveData == null) {
            liveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }

        return liveData as StickyLiveData<T>
    }

    class StickyLiveData<T>(private val eventName: String) : LiveData<T>() {

        var mStickyData: T? = null
        var version = 0

        /**
         * 在主线程分发数据
         */
        fun setStickyData(stickyData: T) {
            mStickyData = stickyData
            setValue(stickyData)
        }

        /**
         * 在任何地方分发数据
         */
        fun postStickyData(stickyData: T) {
            mStickyData = stickyData
            postValue(stickyData)
        }

        override fun setValue(value: T) {
            version++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            version++
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
        }

        /**
         * sticky: Boolean 允许注册的事件是否启用黏性事件
         */
        fun observerSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            owner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    eventMap.remove(eventName)
                }
            })

            super.observe(owner, StickyObserver(this, sticky, observer))
        }

    }

}

class StickyObserver<T>(
    private val stickyLiveData: PerDataBus.StickyLiveData<T>,
    private val sticky: Boolean,
    private val observer: Observer<in T>
) : Observer<T> {

    private var lastVersion = stickyLiveData.version

    override fun onChanged(t: T) {
        if (lastVersion >= stickyLiveData.version) {
            if (sticky && stickyLiveData.mStickyData != null) {
                observer.onChanged(stickyLiveData.mStickyData)
            }
            return
        }

        lastVersion = stickyLiveData.version
        observer.onChanged(t)
    }

}
