package net.lishaoy.biz_home.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import net.lishaoy.biz_home.api.GoodsApi
import net.lishaoy.biz_home.model.GoodsList
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.common.ui.PerAbsListFragment
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.pub_mod.item.GoodsItem

class GoodsListFragment: PerAbsListFragment() {

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var categoryTitle: String = ""

    companion object {
        fun newInstance(categoryId: String, subcategoryId: String): Fragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("subcategoryId", subcategoryId)
            val fragment = GoodsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ARouter.getInstance().inject(this)
        enableLoadMore { loadDate() }
        loadDate()
    }

    override fun onRefresh() {
        super.onRefresh()
        loadDate()
    }

    private fun loadDate() {
        ApiFactory.create(GoodsApi::class.java).queryCategoryGoodsList(categoryId, subcategoryId, pageIndex, 10)
            .enqueue(object : PerCallback<GoodsList> {
                override fun onSuccess(response: PerResponse<GoodsList>) {
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

    private fun updateUI(data: GoodsList) {
        val goods = mutableListOf<GoodsItem>()
        for (goodModel in data.list) {
            val  goodsItem = GoodsItem(goodModel,false)
            goods.add(goodsItem)
        }
        finishRefresh(goods)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context,2)
    }

}