package net.lishaoy.biz_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_order.*
import net.lishaoy.common.route.PerRoute
import net.lishaoy.library.util.PerStatusBar

@Route(path = "/order/main")
class OrderActivity : AppCompatActivity() {

    @JvmField
    @Autowired
    var shopName: String? = null

    @JvmField
    @Autowired
    var shopLogo: String? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsName: String? = null

    @JvmField
    @Autowired
    var goodsImage: String? = null

    @JvmField
    @Autowired
    var goodsPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this, true, translucent = false)
        PerRoute.inject(this)
        setContentView(R.layout.activity_order)

        initView()
    }

    private fun initView() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
    }
}