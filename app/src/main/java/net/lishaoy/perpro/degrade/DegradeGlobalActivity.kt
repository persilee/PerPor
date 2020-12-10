package net.lishaoy.perpro.degrade

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_degrade_global.*
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.perpro.R

@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : PerBaseActivity() {

    @JvmField
    @Autowired
    var degrade_title: String? = null

    @JvmField
    @Autowired
    var degrade_desc: String? = null

    @JvmField
    @Autowired
    var degrade_action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.activity_degrade_global)

        supportActionBar?.hide()

        empty_view.setIcon(R.string.if_unexpected1)
        degrade_title?.let { empty_view.setTitle(it) }
        degrade_desc?.let { empty_view.setDesc(it) }
        empty_view.setHelpAction(listener = View.OnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(degrade_action))
            startActivity(intent)
        })

        btn_back.setOnClickListener { onBackPressed() }
    }
}