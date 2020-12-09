package net.lishaoy.service_login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import net.lishaoy.pub_mod.model.UserProfile

object LoginServiceProvider {

    private var iLoginService =
        ARouter.getInstance().build("/login/service").navigation() as? ILoginService

    fun login(context: Context?, observer: Observer<Boolean>) {
        iLoginService?.login(context, observer)
    }

    fun isLogin(): Boolean {
        return iLoginService?.isLogin() == true
    }

    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean = true
    ) {
        iLoginService?.getUserProfile(lifecycleOwner, observer, onlyCache)
    }

    fun getBoardingPass(): String? {
        return iLoginService?.getBoardingPass() ?: ""
    }

}