package net.lishaoy.biz_order.address

import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.annotation.*

interface AddressApi {

    @GET("address")
    fun queryAddress(
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): PerCall<AddressModel>

    @PUT("address", formPost = false)
    fun addAddress(
        @Filed("area") area: String,
        @Filed("city") city: String,
        @Filed("detail") detail: String,
        @Filed("phoneNum") phoneNum: String,
        @Filed("province") province: String,
        @Filed("receiver") receiver: String
    ): PerCall<String>

    @PUT("address/{id}", formPost = false)
    fun updateAddress(
        @Path("id") id: String,
        @Filed("area") area: String,
        @Filed("city") city: String,
        @Filed("detail") detail: String,
        @Filed("phoneNum") phoneNum: String,
        @Filed("province") province: String,
        @Filed("receiver") receiver: String
    ): PerCall<String>

    @DELETE("address/{id}")
    fun deleteAddress(@Path("id") id: String): PerCall<String>

}