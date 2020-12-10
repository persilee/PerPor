package net.lishaoy.biz_home.list

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_goods_list.*
import net.lishaoy.biz_home.R
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.util.PerStatusBar


@Route(path = "/goods/list")
class GoodsListActivity : PerBaseActivity() {

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var categoryTitle: String = ""

    private val FRAGMENT_TAG = "GOODS_LIST_FRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this,true,translucent = false)
        setContentView(R.layout.activity_goods_list)
        PerRoute.inject(this)

        top_bar_btn_back.setOnClickListener {
            onBackPressed()
        }

        top_bar_title.text = categoryTitle

        var fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            fragment = GoodsListFragment.newInstance(categoryId, subcategoryId)
        }
        val ft = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            ft.add(R.id.goods_container, fragment, FRAGMENT_TAG)
        }
        ft.show(fragment).commitAllowingStateLoss()
    }

}