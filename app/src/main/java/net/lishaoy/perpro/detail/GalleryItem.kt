package net.lishaoy.perpro.detail

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import net.lishaoy.common.view.loadUrl
import net.lishaoy.perpro.model.SliderImage
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class GalleryItem(val sliderImage: SliderImage) : PerDataItem<SliderImage, PerViewHolder>() {
    private var parentWidth: Int = 0

    override fun onBindData(holder: PerViewHolder, position: Int) {
        val imageView = holder.itemView as ImageView
        if (!TextUtils.isEmpty(sliderImage.url)) {
            imageView.loadUrl(sliderImage.url) {
                val drawableWidth = it.intrinsicWidth
                val drawableHeight = it.intrinsicHeight
                val params = imageView.layoutParams ?: RecyclerView.LayoutParams(
                    parentWidth,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
                params.width = parentWidth
                params.height = (drawableHeight / (drawableWidth * 1.0f / parentWidth)).toInt()
                imageView.layoutParams = params
                ViewCompat.setBackground(imageView, it)
            }
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val imageView = ImageView(parent.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setBackgroundColor(Color.WHITE)
        return imageView
    }

    override fun onViewAttachedToWindow(holder: PerViewHolder) {
        val viewGroup = holder.itemView.parent as ViewGroup
        parentWidth = viewGroup.measuredWidth
        val params = holder.itemView.layoutParams
        if (params.width != parentWidth) {
            params.width = parentWidth
            params.height = parentWidth
            holder.itemView.layoutParams = params
        }
    }
}