package net.lishaoy.perpro.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_per_banner_demo.*
import net.lishaoy.perpro.R
import net.lishaoy.ui.banner.IBindAdapter
import net.lishaoy.ui.banner.PerBannerAdapter
import net.lishaoy.ui.banner.PerBannerMo
import net.lishaoy.ui.banner.indicator.PerCircleIndicator
import net.lishaoy.ui.banner.indicator.PerIndicator
import net.lishaoy.ui.banner.indicator.PerNumIndicator
import java.util.*
import kotlin.collections.ArrayList

class PerBannerDemoActivity : AppCompatActivity(), IBindAdapter<BannerMo> {

    private var urls = arrayOf(
        "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
    )
    private var autoPlay = true
    private var indicator: PerIndicator<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_per_banner_demo)

        supportActionBar?.hide()
        init(PerCircleIndicator(this), autoPlay)

        banner_switch.setOnCheckedChangeListener { compoundButton, b ->
            autoPlay = b
            init(indicator, autoPlay)
        }

        switch_indicator.setOnCheckedChangeListener { compoundButton, b ->
            if (indicator is PerCircleIndicator) init(PerNumIndicator(this), autoPlay)
            else init(PerCircleIndicator(this), autoPlay)
        }

    }

    private fun init(indicator: PerIndicator<*>?, autoPlay: Boolean) {
        this.indicator = indicator
        var moList: MutableList<PerBannerMo> = ArrayList()
        for (i in 0..7) {
            var mo = BannerMo()
            mo.url = urls[i % urls.size]
            mo.name = mo.url.substring(mo.url.lastIndexOf('/') + 1, mo.url.lastIndexOf('.'))
            moList.add(mo)
        }
        per_banner.setPerIndicator(indicator);
        per_banner.setAutoPlay(autoPlay)
        per_banner.setIntervalTime(1600)
        per_banner.setBannerData(R.layout.banner_item, moList)
        per_banner.setBindAdapter(this)
    }

    override fun onBind(
        viewHolder: PerBannerAdapter.PerBannerViewHolder?,
        bannerMo: BannerMo?,
        position: Int
    ) {
        var image: ImageView = viewHolder?.findViewById(R.id.iv_image) ?: ImageView(this)
        Glide.with(this@PerBannerDemoActivity).load(bannerMo?.url).into(image)
        var name: TextView = viewHolder?.findViewById(R.id.tv_name) ?: TextView(this)
        name.text = bannerMo?.name
    }

}