package net.lishaoy.biz_home.api

import net.lishaoy.biz_home.model.HomeModel
import net.lishaoy.biz_home.model.TabCategory
import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.CacheStrategy
import net.lishaoy.library.restful.annotation.Filed
import net.lishaoy.library.restful.annotation.GET
import net.lishaoy.library.restful.annotation.Path

interface HomeApi {

    @GET("category/categories")
    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    fun queryTabList(): PerCall<List<TabCategory>>

    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @CacheStrategy cacheStrategy: Int,
        @Path("categoryId") categoryId: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): PerCall<HomeModel>

}