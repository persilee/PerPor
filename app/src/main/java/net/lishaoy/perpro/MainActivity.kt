package net.lishaoy.perpro

import android.os.Bundle
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.perpro.logic.MainActivityLogic

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