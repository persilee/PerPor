package net.lishaoy.perpro.http.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.perpro.model.TabCategory
import org.devio.hi.library.restful.annotation.GET

interface HomeApi {

    @GET("category/categories")
    fun queryTabList(): PerCall<List<TabCategory>>

}