package net.lishaoy.biz_home.notice

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_notice_item.*
import net.lishaoy.biz_home.R
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.utils.DateUtil
import net.lishaoy.library.util.PerRes
import net.lishaoy.pub_mod.model.Notice
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class NoticeItem(private val itemData: Notice) : PerDataItem<Notice, PerViewHolder>() {
    override fun onBindData(holder: PerViewHolder, position: Int) {
        val context = holder.itemView.context
        itemData.apply {
            if (type == "goods") {
                holder.notice_item_icon.text = PerRes.getSting(R.string.if_notice_recommend)
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("goodsId", url)
                    PerRoute.startActivity(context, bundle, PerRoute.Destination.DETAIL_MAIN)
                }
            } else {
                holder.notice_item_icon.text = PerRes.getSting(R.string.if_notice_msg)
                holder.itemView.setOnClickListener {
                    PerRoute.startActivity4Browser(url)
                }
            }
            holder.notice_item_title.text = title
            holder.notice_item_sub_title.text = subtitle
            holder.notice_item_date.text = DateUtil.getMDDate(createTime)

        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_notice_item
    }
}