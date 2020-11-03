package net.lishaoy.perpro.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_per_bottm_layout_demo.*
import net.lishaoy.perpro.R
import net.lishaoy.ui.tab.bottom.PerTabBottomInfo

class PerBottmLayoutDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_per_bottm_layout_demo)

        per_bottom_tab_layout.alpha = 0.66f
        var bottomInfoList: MutableList<PerTabBottomInfo<*>> = ArrayList()

        val homeInfo = PerTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoRecommend = PerTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )

        val infoCategory = PerTabBottomInfo(
            "分类",
            "fonts/iconfont.ttf",
            getString(R.string.if_category),
            null,
            "#ff656667",
            "#ffd44949"
        )

        val infoChat = PerTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoProfile = PerTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#ff656667",
            "#ffd44949"
        )
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)
        per_bottom_tab_layout.inflateInfo(bottomInfoList)
        per_bottom_tab_layout.addTabSelectedChangeListener { _,_,nextInfo ->
            Toast.makeText(this, nextInfo.name, Toast.LENGTH_SHORT).show()
        }
        per_bottom_tab_layout.defaultSelected(infoChat)
    }
}