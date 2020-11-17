package net.lishaoy.perpro.http

import net.lishaoy.library.restful.*
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.lang.IllegalStateException

class RetrofitCallFactory(private val baseUrl: String) : PerCall.Factory {

    private var perConvert: PerConvert
    private var apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()

        apiService = retrofit.create(ApiService::class.java)
        perConvert = GsonConvert()
    }

    override fun newCall(request: PerRequest): PerCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(private val request: PerRequest) : PerCall<T> {
        override fun execute(): PerResponse<T> {
            val realCall = createRealCall(request)
            val response = realCall.execute()
            return parseResponse(response)
        }

        private fun parseResponse(response: Response<ResponseBody>): PerResponse<T> {
            var rawData: String? = null
            if (response.isSuccessful){
                val body = response.body()
                if (body != null) {
                    rawData = body.string()
                }
            } else {
                val body = response.errorBody()
                if (body != null) {
                    rawData = body.string()
                }
            }
            return perConvert.convert(rawData!!, request.returnType!!)
        }

        private fun createRealCall(request: PerRequest): Call<ResponseBody> {
            if (request.httpMethod == PerRequest.METHOD.GET) {
                return apiService.get(request.headers, request.endPointUrl(), request.parameters)
            } else if (request.httpMethod == PerRequest.METHOD.POST) {
                val parameters = request.parameters
                var builder = FormBody.Builder()
                var requestBody: RequestBody? = null
                var jsonObject = JSONObject()
                for ((key, value) in parameters!!) {
                    if (request.formPost) {
                        builder.add(key, value)
                    } else {
                        jsonObject.put(key, value)
                    }
                }
                if (request.formPost) {
                    requestBody = builder.build()
                } else {
                    requestBody = RequestBody.create(
                        MediaType.parse("application/json;utf-8"),
                        jsonObject.toString()
                    )
                }
                return apiService.post(request.headers, request.endPointUrl(), requestBody)
            } else {
                throw IllegalStateException("restful only support GET POST for now, url = ${request.endPointUrl()}")
            }
        }

        override fun enqueue(callback: PerCallback<T>) {
            val createRealCall = createRealCall(request)
            createRealCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailed(throwable = t)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val response = parseResponse(response)
                    callback.onSuccess(response)
                }

            })
        }

    }

    interface ApiService {
        @GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>?
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>
    }
}