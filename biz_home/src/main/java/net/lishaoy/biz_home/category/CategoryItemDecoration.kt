package net.lishaoy.biz_home.category

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.lishaoy.library.util.PerDisplayUtil

class CategoryItemDecoration(val callback: (Int) -> String, val spanCount: Int) :
    RecyclerView.ItemDecoration() {

    private val groupFirstPositions = mutableMapOf<String, Int>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.textSize = PerDisplayUtil.dp2px(15f).toFloat()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition >= parent.adapter!!.itemCount || adapterPosition < 0) return

        val groupName = callback(adapterPosition)
        val preGroupName = if (adapterPosition > 0) callback(adapterPosition - 1) else null

        if (!TextUtils.equals(
                groupName,
                preGroupName
            ) && !groupFirstPositions.containsKey(groupName)
        ) {
            groupFirstPositions[groupName] = adapterPosition
        }

        val firstRowPosition = groupFirstPositions[groupName] ?: 0
        val sameRow = adapterPosition - firstRowPosition in 0 until spanCount

        if (!TextUtils.equals(groupName, preGroupName) || sameRow) {
            outRect.set(0, PerDisplayUtil.dp2px(40f), 0, 0)
            return
        }

        outRect.set(0, 0, 0, 0)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val adapterPosition = parent.getChildAdapterPosition(view)
            if (adapterPosition >= parent.adapter!!.itemCount || adapterPosition < 0) continue
            val groupName = callback(adapterPosition)
            val groupFirstPosition = groupFirstPositions[groupName]
            if (groupFirstPosition == adapterPosition) {
                val decorationBounds = Rect()
                parent.getDecoratedBoundsWithMargins(view, decorationBounds)
                val textBounds = Rect()
                paint.getTextBounds(groupName,0,groupName.length,textBounds)
                c.drawText(groupName, PerDisplayUtil.dp2px(16f).toFloat(), (decorationBounds.top + textBounds.height() * 2).toFloat(), paint )
            }
        }
    }

    fun clear() {
        groupFirstPositions.clear()
    }

}