package net.lishaoy.common.city

import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.GET

internal interface CityApi {

    @GET("cities")
    fun listCities() : PerCall<CityModel>

}