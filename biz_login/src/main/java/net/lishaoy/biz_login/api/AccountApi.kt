package net.lishaoy.biz_login.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.pub_mod.model.UserProfile
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.POST

interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): PerCall<String>

    @POST("user/registration")
    fun register(
        @Filed("userName") userName: String,
        @Filed("password") password: String,
        @Filed("orderId") orderId: String,
        @Filed("imoocId") imoocId: String
    ): PerCall<String>

    @GET("user/profile")
    fun profile(): PerCall<UserProfile>

}