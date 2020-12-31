package net.lishaoy.biz_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.lishaoy.biz_order.address.Address
import net.lishaoy.biz_order.address.AddressApi
import net.lishaoy.biz_order.address.AddressModel
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse

class OrderViewModel: ViewModel() {

    fun queryAddress(): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java).queryAddress(1, 1)
            .enqueue(object : PerCallback<AddressModel> {
                override fun onSuccess(response: PerResponse<AddressModel>) {
                    val list = response.data?.list
                    val firstElement = if (list?.isNotEmpty() == true) list[0] else null
                    liveData.value = firstElement
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.value = null
                }

            })

        return liveData
    }

}