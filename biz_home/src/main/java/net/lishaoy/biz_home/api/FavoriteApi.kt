package net.lishaoy.biz_home.api

import net.lishaoy.biz_home.model.Favorite
import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.POST
import net.lishaoy.library.restful.annotation.Path


interface FavoriteApi {

    @POST("favorites/{goodsId}")
    fun favorite(@Path("goodsId") goodsId: String): PerCall<Favorite>

}