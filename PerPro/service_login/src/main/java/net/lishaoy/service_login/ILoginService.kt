package net.lishaoy.service_login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface ILoginService {

    fun login(context: Context?, observer: Observer<Boolean>)

    fun isLogin(): Boolean

    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean = true
    )

    fun getBoardingPass(): String?

}