package net.lishaoy.biz_search.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.layout_history_search.view.*
import net.lishaoy.biz_search.KeyWord
import net.lishaoy.biz_search.R

class HistorySearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val keywords = ArrayList<KeyWord>()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_history_search, this, true)
        orientation = LinearLayout.VERTICAL
    }

    fun bindData(histories: ArrayList<KeyWord>) {
        if (histories == null) return
        keywords.clear()
        keywords.addAll(histories)
        for (index in 0 until histories.size) {
            val chip: Chip
            val childCount = search_history_container.childCount
            if (index < childCount) {
                chip = search_history_container.getChildAt(index) as Chip
            } else {
                chip = generateChip()
                search_history_container.addView(chip)
            }
            chip.text = histories[index].keyWord
        }
    }

    private fun generateChip(): Chip {
        val chip: Chip = LayoutInflater.from(context).inflate(R.layout.layout_history_search_item, search_history_container, false) as Chip
        chip.isCheckable = true
        chip.isChecked = false
        chip.id = search_history_container.childCount

        return  chip
    }

    fun setOnCheckedChangedListener(callback: (KeyWord) -> Unit) {
        search_history_container.setOnCheckedChangeListener { chipGroup, i ->
            for (index in 0 until chipGroup.childCount) {
                if (search_history_container.getChildAt(index).id == i) {
                    callback(keywords[index])
                    break
                }
            }
        }
    }

    fun setOnHistoryClearListener(callback: () -> Unit) {
        search_history_delete.setOnClickListener {
            search_history_container.removeAllViews()
            keywords.clear()
            callback()
        }
    }

}