package net.lishaoy.perpro.fragment.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import net.lishaoy.common.view.loadUrl
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.perpro.model.HomeBanner
import net.lishaoy.ui.banner.IPerBanner
import net.lishaoy.ui.banner.PerBanner
import net.lishaoy.ui.banner.PerBannerAdapter
import net.lishaoy.ui.banner.PerBannerMo
import net.lishaoy.ui.item.PerDataItem

class BannerItem(val list: List<HomeBanner>): PerDataItem<List<HomeBanner>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val banner  = holder.itemView as PerBanner
        val models = mutableListOf<PerBannerMo>()
        list.forEachIndexed { index, homeBanner ->
            val bannerMo = object : PerBannerMo() {}
            bannerMo.url = homeBanner.cover
            models.add(bannerMo)
        }
        banner.setBannerData(models)
        banner.setOnBannerClickListener(object : IPerBanner.OnBannerClickListener<PerBannerMo> {
            override fun <M : Any?> onBannerClick(
                viewHolder: PerBannerAdapter.PerBannerViewHolder,
                bannerMo: M,
                position: Int
            ) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].url))
                context.startActivity(intent)
            }

        })

        banner.setBindAdapter { viewHolder, bannerMo, position ->
            ((viewHolder.rootView) as ImageView).loadUrl(bannerMo.url)
        }

    }

    override fun getItemView(parent: ViewGroup): View? {
        val context = parent.context
        val banner = PerBanner(context)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, PerDisplayUtil.dp2px(160f, context.resources))
        params.bottomMargin = PerDisplayUtil.dp2px(10f, context.resources)
        banner.layoutParams = params
        banner.setBackgroundColor(Color.WHITE)

        return banner
    }
}