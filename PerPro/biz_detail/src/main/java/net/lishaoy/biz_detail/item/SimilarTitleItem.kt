package net.lishaoy.biz_detail.item

import net.lishaoy.biz_detail.R
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class SimilarTitleItem: PerDataItem<Any, PerViewHolder>() {
    override fun onBindData(holder: PerViewHolder, position: Int) {

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}