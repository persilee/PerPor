package net.lishaoy.biz_home.recommend

import android.os.Bundle
import android.view.View
import net.lishaoy.biz_home.R
import net.lishaoy.common.flutter.PerFlutterCacheManager
import net.lishaoy.common.flutter.PerFlutterFragment
import net.lishaoy.common.ui.PerBaseFragment

class RecommendFragment: PerFlutterFragment(PerFlutterCacheManager.MODULE_RECOMMEND) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("推荐")
    }
}