package net.lishaoy.perpro.detail

import android.text.TextUtils
import androidx.lifecycle.*
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.perpro.BuildConfig
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.DetailApi
import net.lishaoy.perpro.model.DetailModel

class DetailViewModel(val goodsId: String?) : ViewModel() {

    companion object {
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val constructor = modelClass.getConstructor(String::class.java)
                try {
                    if (constructor != null) {
                        return constructor.newInstance(goodsId)
                    }
                } catch (e: Exception) {
                }
                return super.create(modelClass)
            }

        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(DetailViewModel::class.java)
        }
    }

    fun queryDetailData(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!)
                .enqueue(object : PerCallback<DetailModel> {
                    override fun onSuccess(response: PerResponse<DetailModel>) {
                        if (response.successful() && response.data != null) {
                            pageData.postValue(response.data)
                        } else {
                            pageData.postValue(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        pageData.postValue(null)
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace()
                        }
                    }

                })
        }

        return pageData
    }

}