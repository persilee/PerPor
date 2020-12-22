package net.lishaoy.biz_order.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import java.util.*

class AddressViewModel: ViewModel() {

    fun saveAddress(
        province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java).addAddress(area, city, detail, phone, province, receiver)
            .enqueue(object : PerCallback<String> {
                override fun onSuccess(response: PerResponse<String>) {
                    if (response.successful()) {
                        liveData.value = Address(province, city, area, detail, receiver, phone, "", "")
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.value = null
                }

            })
        return liveData
    }

    fun updateAddress(
        id: String,
        province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java).updateAddress(id, area, city, detail, phone, province, receiver)
            .enqueue(object : PerCallback<String> {
                override fun onSuccess(response: PerResponse<String>) {
                    if (response.successful()) {
                        liveData.value = Address(province, city, area, detail, receiver, phone, id, "")
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.value = null
                }

            })
        return liveData
    }

}