package net.lishaoy.ui.slider

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.per_slider_menu_item.view.*
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.ui.R
import net.lishaoy.ui.item.PerViewHolder

class PerSliderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var menuItemAttr: MenuItemAttr
    private val TEXT_COLOR_NORMAL = Color.parseColor("#666666")
    private val TEXT_COLOR_SELECT = Color.parseColor("#DD3127")

    private val BG_COLOR_NORMAL = Color.parseColor("#F7F8F9")
    private val BG_COLOR_SELECT = Color.parseColor("#ffffff")

    val MENU_ITEM_LAYOUT_RES_ID = R.layout.per_slider_menu_item
    val CONTENT_ITEM_LAYOUT_RES_ID = R.layout.per_slider_content_item

    val menuView = RecyclerView(context)
    val contentView = RecyclerView(context)

    init {
        menuItemAttr = parseMenuItemAttr(attrs)
        orientation = HORIZONTAL

        menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        menuView.overScrollMode = View.OVER_SCROLL_NEVER
        menuView.itemAnimator = null

        contentView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.overScrollMode = View.OVER_SCROLL_NEVER
        contentView.itemAnimator = null

        addView(menuView)
        addView(contentView)
    }

    fun bindMenuView(
        layoutRes: Int = MENU_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        onBindView: (PerViewHolder, Int) -> Unit,
        onItemClick: (PerViewHolder, Int) -> Unit
    ) {

        menuView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        menuView.adapter = object : RecyclerView.Adapter<PerViewHolder>() {
            private var lastSelectIndex: Int = 0
            private var currentSelectIndex: Int = 0

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerViewHolder {
                val view = LayoutInflater.from(context).inflate(layoutRes, parent, false)
                val params = RecyclerView.LayoutParams(menuItemAttr.width, menuItemAttr.height)
                view.layoutParams = params
                view.setBackgroundColor(menuItemAttr.backgroundColor)
                view.findViewById<TextView>(R.id.menu_item_title)
                    ?.setTextColor(menuItemAttr.textColor)
                view.findViewById<ImageView>(R.id.menu_item_indicator)
                    ?.setImageDrawable(menuItemAttr.indicator)
                return PerViewHolder(view)
            }

            override fun getItemCount(): Int {
                return itemCount
            }

            override fun onBindViewHolder(holder: PerViewHolder, position: Int) {
                holder.itemView.setOnClickListener {
                    currentSelectIndex = position
                    notifyItemChanged(position)
                    notifyItemChanged(lastSelectIndex)
                }

                applyItemAttr(position, holder)
                onBindView(holder, position)

                if (currentSelectIndex == position) {
                    onItemClick(holder, position)
                    lastSelectIndex = currentSelectIndex
                }
            }

            private fun applyItemAttr(position: Int, holder: PerViewHolder) {
                val selected = position == currentSelectIndex
                val titleView: TextView? = holder.itemView.menu_item_title
                val indicatorView: ImageView? = holder.itemView.menu_item_indicator

                titleView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    if (selected) menuItemAttr.selectTextSize.toFloat() else menuItemAttr.textSize.toFloat()
                )
                titleView?.isSelected = selected
                indicatorView?.visibility = if (selected) View.VISIBLE else View.GONE
                holder.itemView.setBackgroundColor(if (selected) menuItemAttr.selectBackgroundColor else menuItemAttr.backgroundColor)

            }

        }

    }

    fun bindContentView(
        layoutRes: Int = CONTENT_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        itemDecoration: RecyclerView.ItemDecoration?,
        layoutManager: RecyclerView.LayoutManager,
        onBindView: (PerViewHolder, Int) -> Unit,
        onItemClick: (PerViewHolder, Int) -> Unit
    ) {
        if (contentView.layoutManager == null) {
            contentView.layoutManager = layoutManager
            contentView.adapter = object : RecyclerView.Adapter<PerViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerViewHolder {
                    val view = LayoutInflater.from(context).inflate(layoutRes, parent, false)
                    val remainSpace = width - paddingLeft - paddingRight - menuItemAttr.width
                    val layoutManager = (parent as RecyclerView).layoutManager
                    var spanCount = 0
                    if (layoutManager is GridLayoutManager) {
                        spanCount = layoutManager.spanCount
                    } else if (layoutManager is StaggeredGridLayoutManager) {
                        spanCount = layoutManager.spanCount
                    }

                    if (spanCount > 0) {
                        val itemWidth = remainSpace / spanCount
                        view.layoutParams = RecyclerView.LayoutParams(itemWidth, itemWidth)
                    }
                    return  PerViewHolder(view)
                }

                override fun getItemCount(): Int {
                    return itemCount
                }

                override fun onBindViewHolder(holder: PerViewHolder, position: Int) {
                    onBindView(holder, position)
                    holder.itemView.setOnClickListener {
                        onItemClick(holder, position)
                    }
                }
            }
            itemDecoration?.let {
                contentView.addItemDecoration(itemDecoration)
            }
        }
        contentView.scrollToPosition(0)
    }

    private fun parseMenuItemAttr(attrs: AttributeSet?): MenuItemAttr {
        val typeArray =
            context.obtainStyledAttributes(attrs, R.styleable.PerSliderView)
        val menuItemWidth = typeArray.getDimensionPixelOffset(
            R.styleable.PerSliderView_menuItemWidth,
            PerDisplayUtil.dp2px(100f)
        )
        val menuItemHeight = typeArray.getDimensionPixelOffset(
            R.styleable.PerSliderView_menuItemHeight,
            PerDisplayUtil.dp2px(45f)
        )
        val menuItemTextSize = typeArray.getDimensionPixelOffset(
            R.styleable.PerSliderView_menuItemTextSize,
            PerDisplayUtil.dp2px(14f)
        )
        val menuItemSelectTextSize = typeArray.getDimensionPixelOffset(
            R.styleable.PerSliderView_menuItemSelectTextSize,
            PerDisplayUtil.dp2px(14f)
        )
        val menuItemTextColor =
            typeArray.getColorStateList(R.styleable.PerSliderView_menuItemTextColor)
                ?: generateColorStateList()
        val menuItemIndicator = typeArray.getDrawable(R.styleable.PerSliderView_menuItemIndicator)
            ?: ContextCompat.getDrawable(context, R.drawable.shape_slider_indicator)
        val menuItemBackgroundColor =
            typeArray.getColor(R.styleable.PerSliderView_menuItemBackgroundColor, BG_COLOR_NORMAL)
        val menuItemSelectBackgroundColor = typeArray.getColor(
            R.styleable.PerSliderView_menuItemSelectBackgroundColor,
            BG_COLOR_SELECT
        )

        typeArray.recycle()

        return MenuItemAttr(
            menuItemWidth,
            menuItemHeight,
            menuItemTextColor,
            menuItemTextSize,
            menuItemSelectTextSize,
            menuItemBackgroundColor,
            menuItemSelectBackgroundColor,
            menuItemIndicator
        )
    }

    data class MenuItemAttr(
        val width: Int,
        val height: Int,
        val textColor: ColorStateList,
        val textSize: Int,
        val selectTextSize: Int,
        val backgroundColor: Int,
        val selectBackgroundColor: Int,
        val indicator: Drawable?
    )

    private fun generateColorStateList(): ColorStateList {
        val states = Array(2) { intArrayOf(2) }
        val colors = intArrayOf(2)

        colors[0] = TEXT_COLOR_SELECT
        colors[1] = TEXT_COLOR_NORMAL

        states[0] = IntArray(1) { android.R.attr.state_selected }
        states[1] = IntArray(1)

        return ColorStateList(states, colors)
    }

}