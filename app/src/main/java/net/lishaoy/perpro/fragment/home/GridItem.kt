package net.lishaoy.perpro.fragment.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_home_grid_item.*
import kotlinx.android.synthetic.main.layout_home_grid_item.view.*
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.route.PerRoute.Destination
import net.lishaoy.common.route.PerRoute.Destination.*
import net.lishaoy.common.view.loadUrl
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.perpro.R
import net.lishaoy.perpro.model.Subcategory
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class GridItem(val list: List<Subcategory>) :
    PerDataItem<List<Subcategory>, PerViewHolder>(list) {

    override fun onBindData(holder: PerViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter = object : RecyclerView.Adapter<PerViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): PerViewHolder {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_home_grid_item, parent, false)

                return PerViewHolder(view)
            }

            override fun getItemCount(): Int {
                return list.size
            }

            override fun onBindViewHolder(holder: PerViewHolder, position: Int) {
                val subcategory = list[position]
                holder.grid_item_image.loadUrl(subcategory.subcategoryIcon)
                holder.grid_item_title.text = subcategory.subcategoryName
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("categoryId", subcategory.categoryId)
                    bundle.putString("subcategoryId", subcategory.subcategoryId)
                    bundle.putString("categoryTitle", subcategory.subcategoryName)
                    PerRoute.startActivity(context,bundle, GOODS_LIST)
                }
            }
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val gridView = RecyclerView(parent.context)

        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = PerDisplayUtil.dp2px(10f)
        gridView.layoutManager = GridLayoutManager(parent.context, 5)
        gridView.layoutParams = params
        gridView.setBackgroundColor(Color.WHITE)
        return gridView
    }
}