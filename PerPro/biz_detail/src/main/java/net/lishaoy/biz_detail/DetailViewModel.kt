package net.lishaoy.biz_detail

import android.text.TextUtils
import androidx.lifecycle.*
import com.alibaba.android.arouter.BuildConfig
import net.lishaoy.biz_detail.api.DetailApi
import net.lishaoy.biz_detail.api.FavoriteApi
import net.lishaoy.biz_detail.model.DetailModel
import net.lishaoy.biz_detail.model.Favorite
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse

class DetailViewModel(private val goodsId: String?) : ViewModel() {

    companion object {
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    val constructor = modelClass.getConstructor(String::class.java)
                    if(constructor != null) {
                        return constructor.newInstance(goodsId)
                    }
                } catch (e: Exception) {
                }
                return super.create(modelClass)
            }

        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                DetailViewModel::class.java
            )
        }
    }

    fun queryDetailData(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!)
                .enqueue(object : PerCallback<DetailModel> {
                    override fun onSuccess(response: PerResponse<DetailModel>) {
                        if (response.successful() && response.data != null) {
                            pageData.value = response.data
                        } else {
                            pageData.value = null
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        pageData.value = null
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace()
                        }
                    }

                })
        }

        return pageData
    }

    fun toggleFavorite(): LiveData<Boolean?> {
        val toggleFavoriteData = MutableLiveData<Boolean?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(FavoriteApi::class.java).favorite(goodsId!!)
                .enqueue(object : PerCallback<Favorite> {
                    override fun onSuccess(response: PerResponse<Favorite>) {
                        if (response.successful() && response.data != null) {
                            toggleFavoriteData.postValue(response.data?.isFavorite)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        toggleFavoriteData.postValue(null)
                    }

                })
        }
        return toggleFavoriteData
    }

}