package net.lishaoy.biz_detail.api

import net.lishaoy.biz_detail.model.DetailModel
import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.GET
import net.lishaoy.library.restful.annotation.Path


interface DetailApi {

    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId: String): PerCall<DetailModel>

}