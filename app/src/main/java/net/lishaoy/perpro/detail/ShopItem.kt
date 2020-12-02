package net.lishaoy.perpro.detail

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_detail_item_shop.*
import kotlinx.android.synthetic.main.layout_detail_item_shop_goods_item.*
import kotlinx.android.synthetic.main.layout_detail_item_shop_goods_item.view.*
import net.lishaoy.common.view.loadUrl
import net.lishaoy.perpro.R
import net.lishaoy.perpro.fragment.home.GoodsItem
import net.lishaoy.perpro.model.DetailModel
import net.lishaoy.perpro.model.GoodsModel
import net.lishaoy.perpro.model.Shop
import net.lishaoy.ui.item.PerAdapter
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class ShopItem(val detailModel: DetailModel) : PerDataItem<DetailModel, PerViewHolder>() {

    private val SHOP_GOODS_ITEM_COUNT = 3

    override fun onBindData(holder: PerViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val shop: Shop? = detailModel.shop
        shop?.let {
            holder.detail_shop_logo.loadUrl(it.logo)
            holder.detail_shop_title.text = it.name
            holder.detail_shop_desc.text = String.format(
                context.getString(R.string.detail_goods_num),
                it.goodsNum,
                it.completedNum
            )
            val evaluation: String? = shop.evaluation
            evaluation?.let {
                val tagContainer = holder.detail_shop_tag_container
                tagContainer.visibility = View.VISIBLE
                val tags = evaluation.split(" ")
                var index = 0
                for (tagIndex in 0..tags.size / 2) {
                    val tagView = if (tagIndex < tagContainer.childCount) {
                        tagContainer.getChildAt(tagIndex) as TextView
                    } else {
                        val tagView = TextView(context)
                        val params =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        params.weight = 1f
                        tagView.layoutParams = params
                        tagView.gravity = Gravity.CENTER
                        tagView.textSize = 14f
                        tagView.setTextColor(ContextCompat.getColor(context, R.color.color_666))
                        tagView
                    }

                    val name = tags[index]
                    val tag = tags[index + 1]
                    index += 2

                    val spanTag = spanTag(context, name, tag)
                    tagView.text = spanTag

                    tagContainer.addView(tagView)
                }
            }
        }

        val flowGoods: List<GoodsModel>? = detailModel.flowGoods
        flowGoods?.let {
            val flowRecyclerView = holder.detail_shop_recycler_view
            flowRecyclerView.visibility = View.VISIBLE
            if (flowRecyclerView.layoutManager == null) {
                flowRecyclerView.layoutManager = GridLayoutManager(context, SHOP_GOODS_ITEM_COUNT)
            }
            if (flowRecyclerView.adapter == null) {
                flowRecyclerView.adapter = PerAdapter(context)
            }
            val dataItems = mutableListOf<GoodsItem>()
            it.forEach { goodsModel ->
                dataItems.add(ShopGoodsItem(goodsModel))
            }
            val perAdapter = flowRecyclerView.adapter as PerAdapter
            perAdapter.clearItems()
            perAdapter.addItems(dataItems, true)
        }
    }

    private inner class ShopGoodsItem(goodsModel: GoodsModel) : GoodsItem(goodsModel, false) {
        override fun getItemLayoutRes(): Int {
            return R.layout.layout_detail_item_shop_goods_item
        }

        override fun onViewAttachedToWindow(holder: PerViewHolder) {
            super.onViewAttachedToWindow(holder)
            val viewParent: ViewGroup = holder.itemView.parent as ViewGroup
            val availableWidth =
                viewParent.measuredWidth - viewParent.paddingLeft - viewParent.paddingRight
            val itemWidth = availableWidth / SHOP_GOODS_ITEM_COUNT
            val itemImage = holder.detail_shop_good_item_image
            val params = itemImage.layoutParams
            params.width = itemWidth
            params.height = itemWidth
            itemImage.layoutParams = params
        }
    }

    private fun spanTag(context: Context, name: String, tag: String): CharSequence {
        val ss = SpannableString(tag)
        val ssb = SpannableStringBuilder(ss)
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_c61)), 0, ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            BackgroundColorSpan(ContextCompat.getColor(context, R.color.color_f8e)), 0, ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.append(name)
        ssb.append(ss)
        return ssb
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_shop
    }
}