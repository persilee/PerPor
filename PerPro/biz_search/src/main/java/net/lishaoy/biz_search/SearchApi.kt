package net.lishaoy.biz_search

import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.annotation.Filed
import net.lishaoy.library.restful.annotation.GET
import net.lishaoy.library.restful.annotation.POST

interface SearchApi {

    @GET("goods/search/quick")
    fun quickSearch(@Filed("keyWord") keyWord: String): PerCall<SearchModel>

    @POST("goods/search", formPost = false)
    fun goodsSearch(
        @Filed("keyWord") keyWord: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): PerCall<GoodsSearchList>

}