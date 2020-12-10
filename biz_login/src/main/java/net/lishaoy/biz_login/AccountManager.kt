package net.lishaoy.biz_login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import net.lishaoy.biz_login.api.AccountApi
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.cache.PerStorage
import net.lishaoy.library.executor.PerExecutor
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.util.AppGlobals
import net.lishaoy.library.util.SPUtil
import net.lishaoy.service_login.UserProfile
import java.lang.IllegalStateException


object AccountManager {

    private var userProfile: UserProfile? = null
    private var boardingPass: String = ""
    private const val KEY_BOARDING_PASS = "boarding_pass"
    private const val KEY_USER_PROFILE = "user_profile"
    private val loginLiveData = MutableLiveData<Boolean>()
    private val loginForeverObserver = mutableListOf<Observer<Boolean>>()
    private val profileLiveData = MutableLiveData<UserProfile>()
    private val profileForeverObserver = mutableListOf<Observer<UserProfile?>>()

    @Volatile
    private var isFetching = false

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
            loginForeverObserver.add(observer)
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            throw IllegalStateException("context must not be null")
        }
        context.startActivity(intent)
    }

    fun loginSuccess(boardingPass: String) {
        SPUtil.putString(KEY_BOARDING_PASS, boardingPass)
        this.boardingPass = boardingPass
        loginLiveData.value = true
        clearLoginForeverObservers()
    }

    private fun clearLoginForeverObservers() {
        for (observer in loginForeverObserver) {
            loginLiveData.removeObserver(observer)
        }
        loginForeverObserver.clear()
    }

    fun getBoardingPass(): String {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS).toString()
        }

        return boardingPass
    }


    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<net.lishaoy.service_login.UserProfile?>,
        onlyCache: Boolean = true
    ) {
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
            profileForeverObserver.add(observer)
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }

        if (userProfile != null && onlyCache) {
            profileLiveData.postValue(userProfile)
            return
        }

        if (isFetching) return
        isFetching = true
        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : PerCallback<UserProfile> {
                override fun onSuccess(response: PerResponse<UserProfile>) {
                    userProfile = response.data
                    if (response.code == PerResponse.SUCCESS && userProfile != null) {
                        PerExecutor.execute(runnable = Runnable {
                            PerStorage.saveCache(KEY_USER_PROFILE, userProfile)
                            isFetching = false
                        })
                        profileLiveData.value = userProfile
                    } else {
                        profileLiveData.value = null
                    }
                    clearProfileForeverObservers()
                }

                override fun onFailed(throwable: Throwable) {
                    isFetching = false
                    profileLiveData.value = null
                    clearProfileForeverObservers()
                }

            })
    }

    private fun clearProfileForeverObservers() {
        for (observer in profileForeverObserver) {
            profileLiveData.removeObserver(observer)
        }
        profileForeverObserver.clear()
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getBoardingPass())
    }
}