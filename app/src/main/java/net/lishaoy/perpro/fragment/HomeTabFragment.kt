package net.lishaoy.perpro.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.lishaoy.common.ui.PerAbsListFragment
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.perpro.fragment.home.BannerItem
import net.lishaoy.perpro.fragment.home.GoodsItem
import net.lishaoy.perpro.fragment.home.GridItem
import net.lishaoy.perpro.fragment.home.HomeViewModel
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.HomeApi
import net.lishaoy.perpro.model.HomeModel
import net.lishaoy.ui.item.PerDataItem
import org.devio.hi.library.restful.annotation.CacheStrategy

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

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)

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
                updateUI(it)
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
        data.goodsList?.forEachIndexed { index, goodsModel ->
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