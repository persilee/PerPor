package net.lishaoy.biz_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.pub_mod.model.GoodsModel

class SearchViewModel : ViewModel() {

    public var pageIndex = 1

    companion object {
        const val PAGE_SIZE = 10
        const val PAGE_INIT_INDEX = 1
    }

    fun quickSearch(key: String): LiveData<List<KeyWord>?> {

        val liveData = MutableLiveData<List<KeyWord>?>()

        ApiFactory.create(SearchApi::class.java).quickSearch(key)
            .enqueue(object : PerCallback<SearchModel> {
                override fun onSuccess(response: PerResponse<SearchModel>) {
                    liveData.value = response.data?.list
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.value = null
                }

            })

        return liveData
    }

   public val goodsSearchLiveDate = MutableLiveData<List<GoodsModel>?>()

    fun goodsSearch(keyword: String, loadInit: Boolean) {
        if (loadInit) pageIndex = PAGE_INIT_INDEX
        ApiFactory.create(SearchApi::class.java).goodsSearch(keyword, pageIndex, PAGE_SIZE)
            .enqueue(object : PerCallback<GoodsSearchList> {
                override fun onSuccess(response: PerResponse<GoodsSearchList>) {
                    goodsSearchLiveDate.value = response.data?.list
                    pageIndex ++
                }

                override fun onFailed(throwable: Throwable) {
                    goodsSearchLiveDate.value = null
                }

            })
    }

    fun saveHistory(keyWord: KeyWord) {

    }

}