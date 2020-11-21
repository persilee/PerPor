package net.lishaoy.perpro.http.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.perpro.model.Subcategory
import net.lishaoy.perpro.model.TabCategory
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface CategoryApi {

    @GET("category/categories")
    fun queryCategoryList(): PerCall<List<TabCategory>>

    @GET("category/subcategories/{categoryId}")
    fun querySubCategoryList(
        @Path("categoryId") categoryId: String
    ): PerCall<List<Subcategory>>

}