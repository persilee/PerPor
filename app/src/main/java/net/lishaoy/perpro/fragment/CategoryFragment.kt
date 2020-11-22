package net.lishaoy.perpro.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_category.*
import net.lishaoy.common.ui.PerBaseFragment
import net.lishaoy.common.view.loadUrl
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.perpro.R
import net.lishaoy.perpro.fragment.category.CategoryItemDecoration
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.CategoryApi
import net.lishaoy.perpro.model.Subcategory
import net.lishaoy.perpro.model.TabCategory
import net.lishaoy.ui.empty.EmptyView
import net.lishaoy.ui.tab.bottom.PerTabBottomLayout

class CategoryFragment : PerBaseFragment() {
    private val SPAN_COUNT: Int = 3
    private var emptyView: EmptyView? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_category
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PerTabBottomLayout.clipBottomPadding(category)
        queryCategoryList()
    }

    private fun queryCategoryList() {
        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(object : PerCallback<List<TabCategory>> {
                override fun onSuccess(response: PerResponse<List<TabCategory>>) {
                    if (response.successful() && response.data != null) {
                        updateUI(response.data!!)
                    } else {
                        showEmptyView()
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }

            })
    }

    private fun updateUI(data: List<TabCategory>) {
        if (!isAlive) return
        emptyView?.visibility = View.GONE
        slider_view.visibility = View.VISIBLE

        slider_view.bindMenuView(
            itemCount = data.size,
            onBindView = { holder, position ->
                val category = data[position]
                holder.findViewById<TextView>(R.id.menu_item_title)?.text = category.categoryName
            },
            onItemClick = { holder, position ->
                val category = data[position]
                querySubCategoryList(category.categoryId)
            })

    }

    private fun querySubCategoryList(categoryId: String) {
        ApiFactory.create(CategoryApi::class.java).querySubCategoryList(categoryId)
            .enqueue(object : PerCallback<List<Subcategory>> {
                override fun onSuccess(response: PerResponse<List<Subcategory>>) {
                    if (response.successful() && response.data != null) {
                        updateUIContent(response.data!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {

                }

            })
    }

    private val subcategoryList = mutableListOf<Subcategory>()
    private val decoration =
        CategoryItemDecoration({ position -> subcategoryList[position].groupName }, SPAN_COUNT)
    private val layoutManager = GridLayoutManager(context, SPAN_COUNT)
    private val groupSpanSizeOffset = SparseIntArray()
    private val spanSizeLookUp = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName = subcategoryList[position].groupName
            val nextGroupName =
                if (position + 1 < subcategoryList.size) subcategoryList[position + 1].groupName else null
            if (TextUtils.equals(groupName, nextGroupName)) {
                spanSize = 1
            } else {
                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                val lastGroupOffset = if (size <= 0) 0 else if (indexOfKey >= 0) {
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)
                } else {
                    groupSpanSizeOffset.valueAt(size - 1)
                }
                spanSize = SPAN_COUNT - (position + lastGroupOffset) % SPAN_COUNT
                if (indexOfKey < 0) {
                    val groupOffset = lastGroupOffset + spanSize - 1
                    groupSpanSizeOffset.put(position, groupOffset)
                }
            }

            return spanSize
        }

    }

    private fun updateUIContent(data: List<Subcategory>) {
        decoration.clear()
        groupSpanSizeOffset.clear()
        subcategoryList.clear()
        subcategoryList.addAll(data)

        if (layoutManager.spanSizeLookup != spanSizeLookUp) {
            layoutManager.spanSizeLookup = spanSizeLookUp
        }

        slider_view.bindContentView(
            itemCount = data.size,
            layoutManager = layoutManager,
            itemDecoration = decoration,
            onBindView = { holder, position ->
                val subcategory = data[position]
                holder.findViewById<ImageView>(R.id.content_item_image)
                    ?.loadUrl(subcategory.subcategoryIcon)
                holder.findViewById<TextView>(R.id.content_item_title)?.text =
                    subcategory.subcategoryName
            },
            onItemClick = { holder, position ->
                val subcategory = data[position]
                showToast("the sub category id:" + subcategory.subcategoryId)
            }
        )
    }

    private fun showEmptyView() {
        if (!isAlive) return
        slider_view.visibility = View.GONE
        if (emptyView == null) {
            emptyView = EmptyView(context!!)
            emptyView?.setIcon(R.string.if_empty3)
            emptyView?.setDesc(getString(R.string.list_empty_desc))
            emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                queryCategoryList()
            })
            emptyView?.setBackgroundColor(Color.WHITE)
            emptyView?.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            category.addView(emptyView)
            emptyView?.visibility = View.VISIBLE
        }
    }
}