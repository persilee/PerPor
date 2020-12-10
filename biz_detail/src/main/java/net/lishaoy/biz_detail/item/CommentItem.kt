package net.lishaoy.biz_detail.item

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.layout_detail_item_comment.*
import kotlinx.android.synthetic.main.layout_detail_item_comment_item.view.*
import net.lishaoy.biz_detail.R
import net.lishaoy.biz_detail.model.DetailModel
import net.lishaoy.common.view.loadUrl
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder
import kotlin.math.min

class CommentItem(private val detailModel: DetailModel) : PerDataItem<DetailModel, PerViewHolder>() {
    override fun onBindData(holder: PerViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        holder.detail_comment_title.text = detailModel.commentCountTitle
        val commentTag = detailModel.commentTags
        val tags = commentTag.split(" ")
        if (tags.isNotEmpty()) {
            for (tag in tags.indices) {
                val chipGroup = holder.detail_comment_chip_group
                val chip = if (tag < chipGroup.childCount) {
                    chipGroup.getChildAt(tag) as Chip
                } else {
                    val chip = Chip(context)
                    chip.chipCornerRadius = PerDisplayUtil.dp2px(6f).toFloat()
                    chip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.color_faf0
                        )
                    )
                    chip.setTextColor(ContextCompat.getColor(context, R.color.color_999))
                    chip.textSize = 14f
                    chip.gravity = Gravity.CENTER
                    chip.isCheckedIconVisible = false
                    chip.isChecked = false
                    chip.isChipIconVisible = false
                    chip.isCheckable = false
                    holder.detail_comment_chip_group.addView(chip)
                    chip
                }
                chip.text = tags[tag]
            }
        }

        detailModel.commentModels?.let {
            val commentContainer = holder.detail_comment_container
            for (index in 0..min(it.size - 1, 3)) {
                val comment = it[index]
                val itemView = if (index < commentContainer.childCount) {
                    commentContainer.getChildAt(index)
                } else {
                    LayoutInflater.from(context).inflate(R.layout.layout_detail_item_comment_item, commentContainer, false)
                }
                itemView.detail_comment_user_avatar.loadUrl(comment.avatar)
                itemView.detail_comment_user_name.text = comment.nickName
                itemView.detail_comment_content.text = comment.content
                commentContainer.addView(itemView)
            }
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_comment
    }
}