package net.lishaoy.biz_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.library.cache.PerStorage
import net.lishaoy.library.executor.PerExecutor
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.pub_mod.model.GoodsModel

class SearchViewModel : ViewModel() {

    var cache: ArrayList<KeyWord>? = null
    var pageIndex = 1

    companion object {
        const val PAGE_SIZE = 10
        const val PAGE_INIT_INDEX = 1
        const val KEY_SEARCH_HISTORY = "search_history"
        const val MAX_SEARCH_SIZE = 10
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
        if (cache == null) {
            cache = ArrayList()
        }
        cache?.apply {
            if (contains(keyWord)) {
                remove(keyWord)
            }
            add(0, keyWord)
            if (this.size > MAX_SEARCH_SIZE) {
                dropLast(this.size - MAX_SEARCH_SIZE)
            }
            PerExecutor.execute(runnable = Runnable {
                PerStorage.saveCache(KEY_SEARCH_HISTORY, cache)
            })
        }
    }

    fun queryLocalHistory(): LiveData<ArrayList<KeyWord>> {
        val liveData = MutableLiveData<ArrayList<KeyWord>>()
        PerExecutor.execute(runnable = Runnable {
            cache = PerStorage.getCache<ArrayList<KeyWord>>(KEY_SEARCH_HISTORY)
            liveData.postValue(cache)
        })

        return liveData
    }

    fun clearHistory() {
        PerExecutor.execute(runnable = Runnable {
            PerStorage.deleteCache(KEY_SEARCH_HISTORY)
        })
    }

}