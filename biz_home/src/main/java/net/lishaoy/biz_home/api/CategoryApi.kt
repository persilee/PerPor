package net.lishaoy.biz_home.api

import net.lishaoy.biz_home.model.Subcategory
import net.lishaoy.biz_home.model.TabCategory
import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.GET
import net.lishaoy.library.restful.annotation.Path

interface CategoryApi {

    @GET("category/categories")
    fun queryCategoryList(): PerCall<List<TabCategory>>

    @GET("category/subcategories/{categoryId}")
    fun querySubCategoryList(
        @Path("categoryId") categoryId: String
    ): PerCall<List<Subcategory>>

}