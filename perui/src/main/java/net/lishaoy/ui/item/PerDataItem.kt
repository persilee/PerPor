package net.lishaoy.ui.item

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class PerDataItem<DATA, VH: RecyclerView.ViewHolder>(data: DATA) {

    private var adapter: PerAdapter? = null
    var data: DATA ?= null
    init {
        this.data = data
    }

    abstract fun onBindData(holder: RecyclerView.ViewHolder, position: Int)

    open fun getItemLayoutRes() : Int {
        return -1
    }

    open fun getItemView(parent: ViewGroup) : View? {
        return null
    }

    fun setAdapter(adapter: PerAdapter) {
        this.adapter = adapter
    }

    fun getAdapter(): PerAdapter? {
        return adapter
    }

    fun refreshItem() {
        adapter?.refreshItem(this)
    }

    fun removeItem() {
        adapter?.removeItem(this)
    }

    fun getSpanSize() : Int {
        return 0
    }

    /**
     * 该item被滑进屏幕
     */
    open fun onViewAttachedToWindow(holder: VH) {

    }

    /**
     * 该item被滑出屏幕
     */
    open fun onViewDetachedFromWindow(holder: VH) {

    }

    open fun onCreateViewHolder(parent: ViewGroup): VH? {
        return null
    }
}