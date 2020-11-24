package net.lishaoy.perpro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.util.PerStatusBar
import net.lishaoy.perpro.logic.MainActivityLogic

class MainActivity : PerBaseActivity(), MainActivityLogic.ActivityProvider {

    private lateinit var activityLogic: MainActivityLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this,true, Color.WHITE,false)
        setContentView(R.layout.activity_main)
        activityLogic = MainActivityLogic(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityLogic.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var fragments: List<Fragment> = supportFragmentManager.fragments
        for (fragment in fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}