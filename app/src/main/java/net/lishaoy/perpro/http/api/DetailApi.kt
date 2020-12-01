package net.lishaoy.perpro.http.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.perpro.model.DetailModel
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface DetailApi {

    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId: String): PerCall<DetailModel>

}