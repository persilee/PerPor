package net.lishaoy.perpro.detail

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_detail_item_header.*
import net.lishaoy.common.view.loadUrl
import net.lishaoy.perpro.R
import net.lishaoy.perpro.model.DetailModel
import net.lishaoy.perpro.model.SliderImage
import net.lishaoy.ui.banner.PerBannerMo
import net.lishaoy.ui.banner.indicator.PerNumIndicator
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class HeaderItem(
    val sliderImages: List<SliderImage>?,
    val price: String?,
    val completedNumText: String?,
    val goodName: String?
) : PerDataItem<DetailModel, PerViewHolder>() {
    override fun onBindData(holder: PerViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val bannerItems = arrayListOf<PerBannerMo>()
        sliderImages?.forEach {
            val bannerMo = object : PerBannerMo() {}
            bannerMo.url = it.url
            bannerItems.add(bannerMo)
        }
        holder.detail_banner.setPerIndicator(PerNumIndicator(context))
        holder.detail_banner.setBannerData(bannerItems)
        holder.detail_banner.setBindAdapter { viewHolder, bannerMo, _ ->
            val imageView = viewHolder?.rootView as ImageView
            imageView.loadUrl(bannerMo.url)
        }
        holder.detail_price.text = spanPrice(price)
        holder.detail_desc.text = completedNumText
        holder.detail_title.text = goodName
    }

    private fun spanPrice(price: String?): CharSequence {
        if (TextUtils.isEmpty(price)) return ""
        val ss = SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18, true), 1, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_header
    }
}