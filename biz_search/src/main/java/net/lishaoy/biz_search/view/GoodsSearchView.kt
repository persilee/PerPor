package net.lishaoy.biz_search.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import net.lishaoy.common.view.PerRecyclerView
import net.lishaoy.pub_mod.item.GoodsItem
import net.lishaoy.pub_mod.model.GoodsModel
import net.lishaoy.ui.item.PerAdapter

class GoodsSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PerRecyclerView(context, attrs, defStyleAttr) {

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = PerAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(list: List<GoodsModel>, loadInit: Boolean) {
        val dataItems = mutableListOf<GoodsItem>()
        for (goodsModel in list) {
            dataItems.add(GoodsItem(goodsModel, true))
        }
        val perAdapter = adapter as PerAdapter
        if (loadInit) perAdapter.clearItems()
        perAdapter.addItems(dataItems, true)
    }
}