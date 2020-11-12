package net.lishaoy.perpro.http.api

import com.google.gson.JsonObject
import net.lishaoy.library.restful.PerCall
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET

interface TestApi {

    @GET("cities")
    fun listCities(@Filed("name") name: String): PerCall<JsonObject>

}