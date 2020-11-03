package net.lishaoy.perpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.perpro.demo.PerBottmLayoutDemoActivity
import net.lishaoy.perpro.demo.PerLogDemoActivity
import net.lishaoy.perpro.logic.MainActivityLogic
import net.lishaoy.ui.tab.bottom.PerTabBottomInfo

class MainActivity : PerBaseActivity(), MainActivityLogic.ActivityProvider {

    private lateinit var activityLogic : MainActivityLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityLogic = MainActivityLogic(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityLogic.onSaveInstanceState(outState)
    }
}