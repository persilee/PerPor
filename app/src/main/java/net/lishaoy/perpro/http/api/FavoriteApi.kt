package net.lishaoy.perpro.http.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.perpro.model.Favorite
import org.devio.hi.library.restful.annotation.POST
import org.devio.hi.library.restful.annotation.Path


interface FavoriteApi {

    @POST("favorites/{goodsId}")
    fun favorite(@Path("goodsId") goodsId: String): PerCall<Favorite>

}