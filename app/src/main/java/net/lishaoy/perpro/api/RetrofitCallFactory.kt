package net.lishaoy.perpro.api

import net.lishaoy.library.restful.PerCall
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerRequest
import net.lishaoy.library.restful.PerResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

class RetrofitCallFactory(val baseUrl: String): PerCall.Factory {

    private var apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
        
        apiService = retrofit.create(ApiService::class.java)
    }

    override fun newCall(request: PerRequest): PerCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(request: PerRequest) : PerCall<T> {
        override fun execute(): PerResponse<T> {
            TODO("Not yet implemented")
        }

        override fun enqueue(callback: PerCallback<T>) {
            TODO("Not yet implemented")
        }

    }

    interface ApiService {
        @GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>,
            @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>,
            @Url url: String,
            @Body body: RequestBody
        ): Call<ResponseBody>
    }
}