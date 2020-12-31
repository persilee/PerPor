package net.lishaoy.biz_home.api

import net.lishaoy.biz_home.model.GoodsList
import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.Filed
import net.lishaoy.library.restful.annotation.GET
import net.lishaoy.library.restful.annotation.Path

interface GoodsApi {

    @GET("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Filed("subcategoryId") subcategoryId: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): PerCall<GoodsList>

}