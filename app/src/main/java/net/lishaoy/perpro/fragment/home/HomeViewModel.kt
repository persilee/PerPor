package net.lishaoy.perpro.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.HomeApi
import net.lishaoy.perpro.model.HomeModel
import net.lishaoy.perpro.model.TabCategory

class HomeViewModel() : ViewModel() {

    fun queryTabList(): LiveData<List<TabCategory>?> {

//        val categoryTabs = savedState.get<List<TabCategory>?>("categoryTabs")
        val liveData = MutableLiveData<List<TabCategory>?>()

//        if (categoryTabs != null) {
//            liveData.postValue(categoryTabs)
//            return liveData
//        }

        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : PerCallback<List<TabCategory>> {
                override fun onSuccess(response: PerResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        liveData.postValue(data)
//                        savedState.set("categoryTabs", data)
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
//        val categoryList = savedState.get<HomeModel?>("categoryList")

//        if (categoryList != null) {
//            liveData.postValue(categoryList)
//            return liveData
//        }

        ApiFactory.create(HomeApi::class.java)
            .queryTabCategoryList(cacheStrategy, categoryId!!, pageIndex, 10)
            .enqueue(object : PerCallback<HomeModel> {
                override fun onSuccess(response: PerResponse<HomeModel>) {
                    if (response.successful() && response.data != null) {
                        liveData.postValue(response.data)
//                        savedState.set("categoryList", response.data)
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