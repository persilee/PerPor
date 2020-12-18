package net.lishaoy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import net.lishaoy.common.R
import net.lishaoy.library.log.PerLog
import net.lishaoy.ui.item.PerAdapter

open class PerRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var loadMoreScrollListener: LoadMoreScrollListener? = null
    private var footerView: View? = null
    private var isLoading: Boolean = false

    inner class LoadMoreScrollListener(val prefetchSize: Int, val callback: () -> Unit) :
        OnScrollListener() {
        val adapter = getAdapter() as PerAdapter
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (isLoading) return
            val totalItemCount = adapter.itemCount
            if (totalItemCount <= 0) return
            val canScrollVertically = recyclerView.canScrollVertically(1)
            val lastVisibleItem = findLastVisibleItem(recyclerView)
            val firstVisibleItem = findFirstVisibleItem(recyclerView)
            if (lastVisibleItem <= 0) return
            val arriveBottom = lastVisibleItem >= totalItemCount - 1 && firstVisibleItem > 0
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING && (canScrollVertically || arriveBottom)) {
                addFooterView()
            }

            if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                return
            }

            val arrivePrefetchPosition = totalItemCount - lastVisibleItem <= prefetchSize
            if (!arrivePrefetchPosition) return

            isLoading = true
            callback()
        }

        private fun addFooterView() {
            val footerView = getFooterView()
            adapter.addFooterView(footerView)
            if (footerView.parent != null) {
                footerView.post { addFooterView() }
            } else {
                adapter.addFooterView(footerView)
            }
        }
    }


    private fun getFooterView(): View {
        if (footerView == null) {
            footerView =
                LayoutInflater.from(context).inflate(R.layout.layout_footer_loading, this, false)
        }

        return footerView!!
    }

    private fun findLastVisibleItem(recyclerView: RecyclerView): Int {
        return when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                layoutManager.findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                layoutManager.findLastVisibleItemPositions(null)[0]
            }
            else -> -1
        }
    }

    private fun findFirstVisibleItem(recyclerView: RecyclerView): Int {
        return when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                return layoutManager.findFirstVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                return  layoutManager.findFirstVisibleItemPositions(null)[0]
            }
            else -> -1
        }
    }

    fun enableLoadMore(callback: () -> Unit, prefetchSize: Int) {
        if (adapter !is PerAdapter) {
            PerLog.e("enable load more must use PerAdapter")
            return
        }

        loadMoreScrollListener = LoadMoreScrollListener(prefetchSize, callback)
        addOnScrollListener(loadMoreScrollListener!!)
    }

    fun disableLoadMore() {
        if (adapter !is PerAdapter) {
            PerLog.e("enable load more must use PerAdapter")
            return
        }

        val adapter = adapter as PerAdapter
        footerView?.let {
            if (footerView!!.parent != null) {
                adapter.removeFooterView(footerView!!)
            }
        }

        loadMoreScrollListener?.let {
            removeOnScrollListener(loadMoreScrollListener!!)
            loadMoreScrollListener = null
            footerView = null
            isLoading = false
        }

    }

    fun isLoadingMore(): Boolean {
        return isLoading
    }

    fun loadFinished(success: Boolean) {
        if (adapter !is PerAdapter) {
            PerLog.e("enable load more must use PerAdapter")
            return
        }
        isLoading = false
        val adapter = adapter as PerAdapter
        if (!success) {
            footerView?.let {
                if (footerView!!.parent != null) {
                    adapter.removeFooterView(footerView!!)
                }
            }
        } else {

        }
    }

}