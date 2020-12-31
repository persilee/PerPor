package net.lishaoy.biz_home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import net.lishaoy.biz_home.api.HomeApi
import net.lishaoy.biz_home.model.HomeModel
import net.lishaoy.biz_home.model.TabCategory
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.restful.annotation.CacheStrategy

class HomeViewModel(var savedState: SavedStateHandle) : ViewModel() {

    fun queryTabList(): LiveData<List<TabCategory>?> {

        val categoryTabs = savedState.get<List<TabCategory>?>("categoryTabs")
        val liveData = MutableLiveData<List<TabCategory>?>()

        if (categoryTabs != null) {
            liveData.postValue(categoryTabs)
            return liveData
        }

        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : PerCallback<List<TabCategory>> {
                override fun onSuccess(response: PerResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        liveData.value = data
                        savedState.set("categoryTabs", data)
                    }
                }

                override fun onFailed(throwable: Throwable) {

                }

            })

        return liveData
    }

    fun queryTabCategoryList(
        categoryId: String?,
        pageIndex: Int,
        cacheStrategy: Int
    ): LiveData<HomeModel?> {

        val liveData = MutableLiveData<HomeModel?>()
        val categoryList = savedState.get<HomeModel?>("categoryList")

        if (categoryList != null && pageIndex == 1 && cacheStrategy == CacheStrategy.CACHE_FIRST) {
            liveData.postValue(categoryList)
            return liveData
        }

        ApiFactory.create(HomeApi::class.java)
            .queryTabCategoryList(cacheStrategy, categoryId!!, pageIndex, 10)
            .enqueue(object : PerCallback<HomeModel> {
                override fun onSuccess(response: PerResponse<HomeModel>) {
                    if (response.successful() && response.data != null) {
                        liveData.value = response.data
                        if (cacheStrategy != CacheStrategy.NET_ONLY && response.code == PerResponse.SUCCESS) {
                            savedState.set("categoryList", response.data)
                        }
                    } else {
                        liveData.postValue(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }

            })

        return liveData
    }

}