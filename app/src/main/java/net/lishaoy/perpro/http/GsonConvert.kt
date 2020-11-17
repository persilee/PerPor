package net.lishaoy.perpro.http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.lishaoy.library.restful.PerConvert
import net.lishaoy.library.restful.PerResponse
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.lang.reflect.Type

class GsonConvert : PerConvert {

    private var gson: Gson

    init {
        gson = Gson()
    }

    override fun <T> convert(rawData: String, dataType: Type): PerResponse<T> {
        var response: PerResponse<T> = PerResponse()
        try {
            var jsonObject = JSONObject(rawData)
            response.code = jsonObject.optInt("code")
            response.msg = jsonObject.optString("msg")
            val data = jsonObject.opt("data")
            if ((data is JSONObject) or (data is JSONArray)) {
                if (response.code == PerResponse.SUCCESS) {
                    response.data = gson.fromJson(data.toString(), dataType)
                } else {
                    response.errorData = gson.fromJson<MutableMap<String, String>>(
                        data.toString(),
                        object : TypeToken<MutableMap<String, String>>() {}.type
                    )
                }
            } else {
                response.data = data as T?
            }

        } catch (e: Exception) {
            e.printStackTrace()
            response.code = -1
            response.msg = e.message
        }
        response.rawData = rawData
        return response
    }
}