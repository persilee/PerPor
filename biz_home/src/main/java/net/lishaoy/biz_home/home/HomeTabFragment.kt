package net.lishaoy.biz_home.home

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.lishaoy.biz_home.model.HomeModel
import net.lishaoy.common.ui.PerAbsListFragment
import net.lishaoy.library.restful.annotation.CacheStrategy
import net.lishaoy.pub_mod.item.GoodsItem
import net.lishaoy.ui.item.PerDataItem

class HomeTabFragment : PerAbsListFragment() {

    private lateinit var viewModel: HomeViewModel
    private var categoryId: String? = null
    val DEFAULT_TAB_CATEGORY_ID = "1"

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("categoryId", DEFAULT_TAB_CATEGORY_ID)
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, SavedStateViewModelFactory(this.activity!!.application, this)).get(HomeViewModel::class.java)

        queryTabCategoryList(CacheStrategy.CACHE_FIRST)

        enableLoadMore { queryTabCategoryList(CacheStrategy.NET_ONLY) }
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_TAB_CATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(context, 2)
    }

    private fun queryTabCategoryList(cacheStrategy: Int) {
        viewModel.queryTabCategoryList(categoryId, pageIndex, cacheStrategy).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateUI(it as HomeModel)
            } else {
                finishRefresh(null)
            }
        })
    }

    private fun updateUI(data: HomeModel) {
        if (!isAlive) return
        val dateItems = mutableListOf<PerDataItem<*, *>>()
        data.bannerList?.let {
            dateItems.add(BannerItem(it))
        }
        data.subcategoryList?.let {
            dateItems.add(GridItem(it))
        }
        data.goodsList?.forEachIndexed { _, goodsModel ->
            dateItems.add(
                GoodsItem(
                    goodsModel,
                    TextUtils.equals(categoryId, DEFAULT_TAB_CATEGORY_ID)
                )
            )
        }
        finishRefresh(dateItems)
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList(CacheStrategy.NET_CACHE)
    }

}