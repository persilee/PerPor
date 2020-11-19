package net.lishaoy.common.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import net.lishaoy.common.R
import net.lishaoy.common.view.PerRecyclerView
import net.lishaoy.ui.empty.EmptyView
import net.lishaoy.ui.item.PerAdapter
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.refresh.PerOverView
import net.lishaoy.ui.refresh.PerRefresh
import net.lishaoy.ui.refresh.PerRefreshLayout
import net.lishaoy.ui.refresh.PerTextOverView

open class PerAbsListFragment : PerBaseFragment(), PerRefresh.PerRefreshListener {

    private var pageIndex: Int = 1
    private lateinit var adapter: PerAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var refreshHeaderView: PerTextOverView
    private var loadingBar: ContentLoadingProgressBar? = null
    private var emptyView: EmptyView? = null
    private var recyclerView: PerRecyclerView? = null
    private var refreshLayout: PerRefreshLayout? = null

    companion object {
        const val PREFETCH_SIZE: Int = 5
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshLayout = refresh_layout
        recyclerView = recycler_view
        emptyView = empty_view
        loadingBar = loading_bar

        refreshHeaderView = context?.let { PerTextOverView(it) }!!
        refreshLayout?.setRefreshOverView(refreshHeaderView)
        refreshLayout?.setRefreshListener(this)

        layoutManager = createLayoutManager()
        adapter = PerAdapter(context!!)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        emptyView?.visibility = View.GONE
        emptyView?.setIcon(R.string.list_empty)
        emptyView?.setDesc(getString(R.string.list_empty_desc))
        emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
            onRefresh()
        })

        loadingBar?.visibility = View.VISIBLE
        pageIndex = 1
    }

    fun finishRefresh(dataItems: List<PerDataItem<*, RecyclerView.ViewHolder>>?) {
        val success = dataItems != null && dataItems.isNotEmpty()
        val refresh = pageIndex == 1
        if (refresh) {
            loadingBar?.visibility = View.GONE
            if (success) {
                loadingBar?.visibility = View.GONE
                refreshLayout?.refreshFinished()
                adapter.clearItems()
                if (dataItems != null) {
                    adapter.addItems(dataItems, true)
                }
            } else {
                if (adapter.itemCount <= 0) {
                    emptyView?.visibility = View.VISIBLE
                }
            }
        } else {
            if (success) {
                adapter.addItems(dataItems!!, true)
            }
            recyclerView?.loadFinished(success)
        }
    }

    fun enableLoadMore(callback: () -> Unit) {
        recyclerView?.enableLoadMore({
            if (refreshHeaderView.state == PerOverView.PerRefreshState.STATE_REFRESH) {
                recyclerView?.loadFinished(false)
                return@enableLoadMore
            }
            pageIndex++
            callback()
        }, PREFETCH_SIZE)
    }

    fun disableLoadMore() {
        recyclerView?.disableLoadMore()
    }

    open fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun enableRefresh(): Boolean {
        return true
    }

    @CallSuper
    override fun onRefresh() {
        if (recyclerView?.isLoadingMore() == true) {
            refreshLayout?.post {
                refreshLayout?.refreshFinished()
            }
            return
        }
        pageIndex = 1
    }
}