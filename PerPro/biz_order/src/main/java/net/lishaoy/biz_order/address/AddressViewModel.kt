package net.lishaoy.biz_order.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.lishaoy.common.ext.showToast
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import java.util.*

class AddressViewModel: ViewModel() {

    var checkPosition: Int? = -1
    var checkedAddressItem: AddressItem? = null

    fun queryAddressList(): LiveData<List<Address>?> {
        val liveData = MutableLiveData<List<Address>?>()
        ApiFactory.create(AddressApi::class.java)
            .queryAddress(1, 100)
            .enqueue(object : PerCallback<AddressModel> {
                override fun onSuccess(response: PerResponse<AddressModel>) {
                    liveData.postValue(response.data?.list)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }

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

    fun deleteAddress(addressId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        ApiFactory.create(AddressApi::class.java).deleteAddress(addressId)
            .enqueue(object : PerCallback<String> {
                override fun onSuccess(response: PerResponse<String>) {
                    if (response.successful()) {
                        liveData.postValue(response.successful())
                    } else {
                        showToast("地址删除失败")
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast("地址删除失败:" + throwable.message)
                }
            })
        return liveData
    }

    override fun onCleared() {
        checkPosition = -1
        checkedAddressItem = null
        super.onCleared()
    }

}