package net.lishaoy.perpro.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.JsonObject
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.perpro.R
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.TestApi

class ApiTestActivity : PerBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_test)
        supportActionBar?.hide()

        ApiFactory.create(TestApi::class.java).listCities("")
            .enqueue(object : PerCallback<JsonObject> {
                override fun onSuccess(response: PerResponse<JsonObject>) {
                    TODO("Not yet implemented")
                }

                override fun onFailed(throwable: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}