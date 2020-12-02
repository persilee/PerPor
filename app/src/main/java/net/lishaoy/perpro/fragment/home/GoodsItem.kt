package net.lishaoy.perpro.fragment.home

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_home_goods_list_item_h.view.*
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.view.loadUrl
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.perpro.R
import net.lishaoy.perpro.model.GoodsModel
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

open class GoodsItem(val goods: GoodsModel, val hotTab: Boolean) :
    PerDataItem<GoodsModel, PerViewHolder>(goods) {
    override fun onBindData(holder: PerViewHolder, position: Int) {
        val MAX_TAG_SIZE = 3
        val context = holder.itemView.context
        goods.sliderImage?.let { holder.itemView.good_item_image.loadUrl(it) }
        holder.itemView.good_item_title.text = goods.goodsName
        holder.itemView.good_item_price.text = goods.marketPrice
        holder.itemView.good_item_desc.text = goods.completedNumText

        val labelContainer = holder.itemView.good_item_label_container
        if (labelContainer != null) {
            if (!TextUtils.isEmpty(goods.tags)) {
                labelContainer.visibility = View.VISIBLE
                val label = goods.tags?.split(" ")
                if (label != null) {
                    for (index in label.indices) {
                        val childCount = labelContainer.childCount
                        if (index > MAX_TAG_SIZE - 1) {
                            for (index in childCount - 1 downTo MAX_TAG_SIZE) {
                                labelContainer.removeViewAt(index)
                            }
                            break
                        }
                        val labelView: TextView =
                            if (index > labelContainer.childCount - 1) {
                                val view = createLabelView(context)
                                view.text = label[index]
                                labelContainer.addView(view)
                                view
                            } else {
                                labelContainer.getChildAt(index) as TextView
                            }

                    }
                }
            } else {
                labelContainer.visibility = View.GONE
            }
        }

        if (!hotTab) {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = getAdapter()?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = getAdapter()?.getAttachRecyclerView()?.paddingLeft ?: 0
            val itemLeft = holder.itemView.left
            if (itemLeft == parentLeft + parentPaddingLeft) {
                params.rightMargin = PerDisplayUtil.dp2px(2f)
            } else {
                params.leftMargin = PerDisplayUtil.dp2px(2f)
            }

            holder.itemView.layoutParams = params
        }

        holder.itemView.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("goodsId", goods.goodsId)
            bundle.putParcelable("goodsModel", goods)
            PerRoute.startActivity(context,bundle,PerRoute.Destination.DETAIL_MAIN)
        }
    }

    fun createLabelView(context: Context): TextView {
        val labelView = TextView(context)
        labelView.setTextColor(ContextCompat.getColor(context, R.color.color_e75))
        labelView.background = ContextCompat.getDrawable(context,R.drawable.shape_goods_label)
        labelView.textSize = 10f
        labelView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            PerDisplayUtil.dp2px(14f)
        )
        params.rightMargin = PerDisplayUtil.dp2px(10f)
        labelView.layoutParams = params

        return labelView
    }

    override fun getItemLayoutRes(): Int {
        return if (hotTab) R.layout.layout_home_goods_list_item_h else R.layout.layout_home_goods_list_item_v
    }

    override fun getSpanSize(): Int {
        return if(hotTab) super.getSpanSize() else 1
    }

}