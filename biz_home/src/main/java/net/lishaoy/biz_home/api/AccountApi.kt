package net.lishaoy.biz_home.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.Filed
import net.lishaoy.library.restful.annotation.GET
import net.lishaoy.library.restful.annotation.POST
import net.lishaoy.pub_mod.model.CourseNotice
import net.lishaoy.pub_mod.model.UserProfile


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

    @GET("notice")
    fun notice(): PerCall<CourseNotice>

}