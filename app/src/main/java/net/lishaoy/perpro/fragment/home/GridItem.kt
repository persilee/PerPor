package net.lishaoy.perpro.fragment.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_home_grid_item.view.*
import net.lishaoy.common.view.loadUrl
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.perpro.R
import net.lishaoy.perpro.model.Subcategory
import net.lishaoy.ui.item.PerDataItem

class GridItem(val list: List<Subcategory>) :
    PerDataItem<List<Subcategory>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_home_grid_item, parent, false)

                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun getItemCount(): Int {
                return list.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val subcategory = list[position]
                holder.itemView.grid_item_image.loadUrl(subcategory.subcategoryIcon)
                holder.itemView.grid_item_title.text = subcategory.subcategoryName
                holder.itemView.setOnClickListener {
                    
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