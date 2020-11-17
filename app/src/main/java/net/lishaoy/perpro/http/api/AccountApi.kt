package net.lishaoy.perpro.http.api

import net.lishaoy.library.restful.PerCall
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.POST

interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): PerCall<String>

}