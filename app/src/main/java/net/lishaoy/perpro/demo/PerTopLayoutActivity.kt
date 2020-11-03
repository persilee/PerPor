package net.lishaoy.perpro.demo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_per_top_layout.*
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.perpro.R
import net.lishaoy.ui.tab.top.PerTabTopInfo

class PerTopLayoutActivity : PerBaseActivity() {

    val array = arrayOf(
        "热门",
        "服装",
        "数码",
        "鞋子",
        "零食",
        "家电",
        "百货",
        "家具",
        "生活",
        "日用"
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_per_top_layout)
        var infoList: MutableList<PerTabTopInfo<*>> = ArrayList()
        var defaultColor = getColor(R.color.tabDefaultColor)
        var tintColor = getColor(R.color.tabTintColor)
        array.forEach {
           var info = PerTabTopInfo(it,defaultColor,tintColor)
            infoList.add(info)
        }
        tab_top_layout.inflateInfo(infoList)
        tab_top_layout.addTabSelectedChangeListener{ _,_,nextInfo ->
            showToast(nextInfo.name)
        }
        tab_top_layout.defaultSelected(infoList[0])
    }
}