package net.lishaoy.perpro.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.lishaoy.common.ui.PerAbsListFragment
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.perpro.fragment.home.BannerItem
import net.lishaoy.perpro.fragment.home.GoodsItem
import net.lishaoy.perpro.fragment.home.GridItem
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.HomeApi
import net.lishaoy.perpro.model.HomeModel
import net.lishaoy.ui.item.PerDataItem

class HomeTabFragment : PerAbsListFragment() {

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

        queryTabCategoryList()

        enableLoadMore { queryTabCategoryList() }
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_TAB_CATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(context, 2)
    }

    private fun queryTabCategoryList() {
        ApiFactory.create(HomeApi::class.java).queryTabCategoryList(categoryId!!, 1, 10)
            .enqueue(object : PerCallback<HomeModel> {
                override fun onSuccess(response: PerResponse<HomeModel>) {
                    if (response.successful() && response.data != null) {
                        updateUI(response.data!!)
                    } else {
                        finishRefresh(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    finishRefresh(null)
                }

            })
    }

    private fun updateUI(data: HomeModel) {
        if (!isAlive) return
        val dateItems = mutableListOf<PerDataItem<*, *>>()
        data.bannerList?.let {
            dateItems.add(BannerItem(data.bannerList))
        }
        data.subcategoryList?.let {
            dateItems.add(GridItem(data.subcategoryList))
        }
        data.goodsList?.forEachIndexed { index, goodsModel ->
            dateItems.add(
                GoodsItem(
                    data.goodsList[index],
                    TextUtils.equals(categoryId, DEFAULT_TAB_CATEGORY_ID)
                )
            )
        }
        finishRefresh(dateItems)
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList()
    }

}