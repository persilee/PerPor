package net.lishaoy.biz_detail.item

import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.layout_detail_item_attr.*
import net.lishaoy.biz_detail.R
import net.lishaoy.biz_detail.model.DetailModel
import net.lishaoy.ui.input.InputItemLayout
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class GoodsAttrItem(private val detailModel: DetailModel) :
    PerDataItem<DetailModel, PerViewHolder>(detailModel) {
    override fun onBindData(holder: PerViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val goodAttr = detailModel.goodAttr
        val attrContainer = holder.detail_attr_container
        attrContainer.visibility = View.VISIBLE
        var index = 0
        goodAttr?.let {
            val iterator = it.iterator()
            while (iterator.hasNext()) {
                val attr = iterator.next()
                val entries = attr.entries
                val key = entries.first().key
                val value = entries.first().value

                val attrItemView = if (index < attrContainer.childCount) {
                    attrContainer.getChildAt(index) as InputItemLayout
                } else {
                    val view = LayoutInflater.from(context)
                        .inflate(
                            R.layout.layout_detail_item_attr_item,
                            attrContainer,
                            false
                        ) as InputItemLayout
                    attrContainer.addView(view)
                    view
                }
                attrItemView.getEditText().hint = value
                attrItemView.getEditText().isEnabled = false
                attrItemView.getTitleView().text = key
                index++
            }
        }

        detailModel.goodDescription.let {
            val detailAttrDesc = holder.detail_attr_desc
            detailAttrDesc.visibility = View.VISIBLE
            detailAttrDesc.text = it
        }

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_attr
    }
}