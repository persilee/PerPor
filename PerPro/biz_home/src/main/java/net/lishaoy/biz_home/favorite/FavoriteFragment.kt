package net.lishaoy.biz_home.favorite

import android.os.Bundle
import android.view.View
import net.lishaoy.common.flutter.PerFlutterCacheManager
import net.lishaoy.common.flutter.PerFlutterFragment

class FavoriteFragment : PerFlutterFragment("nativeView") {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("收藏")
    }

}