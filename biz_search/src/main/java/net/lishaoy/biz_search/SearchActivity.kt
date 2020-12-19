package net.lishaoy.biz_search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_search.*
import net.lishaoy.biz_search.view.GoodsSearchView
import net.lishaoy.biz_search.view.HistorySearchView
import net.lishaoy.biz_search.view.QuickSearchView
import net.lishaoy.common.route.PerRoute
import net.lishaoy.library.log.PerLog
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.library.util.PerRes
import net.lishaoy.library.util.PerStatusBar
import net.lishaoy.ui.empty.EmptyView
import net.lishaoy.ui.search.PerSearchView
import net.lishaoy.ui.search.SimpleTextWatcher

@Route(path = "/search/main")
class SearchActivity : AppCompatActivity() {
    private var historySearchView: HistorySearchView? = null
    private var goodsSearchView: GoodsSearchView? = null
    private var quickSearView: QuickSearchView? = null
    private var emptyView: EmptyView? = null
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchButton: Button
    private lateinit var searchView: PerSearchView

    private var status = -1

    companion object {
        const val STATUS_EMPTY = 0
        const val STATUS_HISTORY = 1
        const val STATUS_QUICK_SEARCH = 2
        const val STATUS_GOODS_SEARCH = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this, true, translucent = false)
        PerRoute.inject(this)
        setContentView(R.layout.activity_search)

        searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SearchViewModel::class.java]
        initTopBar()
        updateViewStatus(STATUS_EMPTY)
        queryLocalHistory()
    }

    private fun queryLocalHistory() {
        searchViewModel.queryLocalHistory().observe(this, Observer {
            if (it != null) {
                updateViewStatus(STATUS_HISTORY)
                historySearchView?.bindData(it)
            } else {
                searchView.editText?.requestFocus()
            }
        })
    }

    private fun updateViewStatus(newStatus: Int) {
        if (status == newStatus) return
        status = newStatus

        var showView: View? = null
        when (status) {
            STATUS_EMPTY -> {
                if (emptyView == null) {
                    emptyView = EmptyView(this)
                    emptyView?.setDesc(PerRes.getSting(R.string.list_empty_desc))
                    emptyView?.setIcon(R.string.list_empty)
                }
                showView = emptyView
            }
            STATUS_QUICK_SEARCH -> {
                if (quickSearView == null) {
                    quickSearView = QuickSearchView(this)
                }
                showView = quickSearView
            }
            STATUS_GOODS_SEARCH -> {
                if (goodsSearchView == null) {
                    goodsSearchView = GoodsSearchView(this)
                    goodsSearchView?.enableLoadMore({
                        searchView.getKeyword()?.let { searchViewModel.goodsSearch(it, false) }
                    }, 6)
                }
                showView = goodsSearchView
            }
            STATUS_HISTORY -> {
                if (historySearchView == null) {
                    historySearchView = HistorySearchView(this)
                    historySearchView!!.setOnCheckedChangedListener {
                        doKeywordSearch(it)
                    }
                    historySearchView!!.setOnHistoryClearListener {
                        searchViewModel.clearHistory()
                        updateViewStatus(STATUS_EMPTY)
                    }
                }
                showView = historySearchView
            }
        }
        if (showView != null) {
            if (showView.parent == null) {
                search_container.addView(showView)
            }

            val childCount = search_container.childCount
            for (index in 0 until childCount) {
                val view = search_container.getChildAt(index)
                view.visibility = if (view == showView) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initTopBar() {
        search_nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
       searchButton =
            search_nav_bar.addRightTextButton(R.string.nav_item_search, R.id.id_nav_item_search)
        searchButton.setTextColor(PerRes.getColorStateList(R.color.color_nav_item_search))
        searchButton.isEnabled = false
        searchButton.setOnClickListener(searchClickListener)

        searchView = PerSearchView(this)
        searchView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, PerDisplayUtil.dp2px(38f))
        searchView.setHintText(PerRes.getSting(R.string.search_hint))
        searchView.setClearIconClickListener(updateHistoryListener)
        searchView.setDebounceTextChangedListener(debounceTextWatcher)

        search_nav_bar.setCenterView(searchView)
    }

    private val searchClickListener = View.OnClickListener {
        val keyword = searchView.editText?.text?.trim().toString()
        if (TextUtils.isEmpty(keyword)) return@OnClickListener
        doKeywordSearch(KeyWord(null, keyword))
    }

    private val updateHistoryListener = View.OnClickListener {
        if (searchViewModel.cache.isNullOrEmpty()) {
            updateViewStatus(STATUS_EMPTY)
        } else {
            updateViewStatus(STATUS_HISTORY)
            historySearchView?.bindData(searchViewModel.cache!!)
        }
    }

    private val debounceTextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            PerLog.i(s.toString())
            val hasContent = s != null && s.trim().isNotEmpty()
            searchButton.isEnabled = hasContent
            if (hasContent) {
                searchViewModel.quickSearch(s.toString()).observe(this@SearchActivity, Observer {
                    if (it.isNullOrEmpty()) {
                        updateViewStatus(STATUS_EMPTY)
                    } else {
                        updateViewStatus(STATUS_QUICK_SEARCH)
                        quickSearView?.bindData(it) { keyWord ->
                            doKeywordSearch(keyWord)
                        }
                    }
                })
            }


        }
    }

    private fun doKeywordSearch(keyWord: KeyWord) {
        searchView.setKeyword(keyWord.keyWord, updateHistoryListener)
        searchViewModel.saveHistory(keyWord)
        val clearIcon = searchView.findViewById<View>(R.id.id_search_clear_icon)
        clearIcon.isEnabled = false
        searchViewModel.goodsSearch(keyWord.keyWord, true)
        searchViewModel.goodsSearchLiveDate.observe(this, Observer {
            clearIcon.isEnabled = true
            goodsSearchView?.loadFinished(!it.isNullOrEmpty())
            val loadInit = searchViewModel.pageIndex == SearchViewModel.PAGE_INIT_INDEX
            if (it == null) {
                if (loadInit) {
                    updateViewStatus(STATUS_EMPTY)
                }
            } else {
                updateViewStatus(STATUS_GOODS_SEARCH)
                goodsSearchView?.bindData(it, loadInit)
            }
        })
    }
}