package net.lishaoy.biz_search.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_quick_search_list_item.view.*
import net.lishaoy.biz_search.KeyWord
import net.lishaoy.biz_search.R
import net.lishaoy.ui.item.PerAdapter
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class QuickSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = PerAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(keywords: List<KeyWord>, callback: (KeyWord) -> Unit) {
        val dataItems = mutableListOf<QuickSearchItem>()
        for (keyword in keywords) {
            dataItems.add(QuickSearchItem(keyword, callback))
        }
        val perAdapter = adapter as PerAdapter
        perAdapter.clearItems()
        perAdapter.addItems(dataItems, false)
    }

    private inner class QuickSearchItem(val keyword: KeyWord, val callback: (KeyWord) -> Unit) :
        PerDataItem<KeyWord, PerViewHolder>() {
        override fun onBindData(holder: PerViewHolder, position: Int) {
            holder.itemView.search_item_title.text = keyword.keyWord
            holder.itemView.setOnClickListener {
                callback(keyword)
            }
        }

        override fun getItemLayoutRes(): Int {
            return R.layout.layout_quick_search_list_item
        }

    }

}