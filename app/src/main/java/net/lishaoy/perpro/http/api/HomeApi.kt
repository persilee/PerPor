package net.lishaoy.perpro.http.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.perpro.model.HomeModel
import net.lishaoy.perpro.model.TabCategory
import org.devio.hi.library.restful.annotation.CacheStrategy
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

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