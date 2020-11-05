package net.lishaoy.ui.banner.indicator

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import net.lishaoy.library.util.PerDisplayUtil

class PerNumIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), PerIndicator<FrameLayout> {

    companion object {
        private const val VMC = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private var pointHorizontalPadding = 0
    private var pointVerticalPadding = 0

    init {
        pointHorizontalPadding = PerDisplayUtil.dp2px(8f, context.resources)
        pointVerticalPadding = PerDisplayUtil.dp2px(12f, context.resources)
    }

    override fun get(): FrameLayout {
        return this
    }

    override fun onInflate(count: Int) {
        removeAllViews()
        if (count <= 0) return
        var layout: LinearLayout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.setPadding(0, 0, pointHorizontalPadding, pointVerticalPadding)
        var textNum: TextView = TextView(context)
        textNum.text = "1"
        textNum.setTextColor(Color.WHITE)
        textNum.textSize = 14f
        layout.addView(textNum)

        var textMid: TextView = TextView(context)
        textMid.text = "/"
        textMid.setTextColor(Color.WHITE)
        textMid.textSize = 14f
        layout.addView(textMid)

        var textCount: TextView = TextView(context)
        textCount.text = count.toString()
        textCount.setTextColor(Color.WHITE)
        textCount.textSize = 14f
        layout.addView(textCount)

        var layoutParams: LayoutParams = LayoutParams(VMC, VMC)
        layoutParams.gravity = Gravity.END or Gravity.BOTTOM
        addView(layout, layoutParams)
    }

    override fun onPointChange(current: Int, count: Int) {
        var viewGroup: ViewGroup = getChildAt(0) as ViewGroup
        var textNum : TextView = viewGroup.getChildAt(0) as TextView
        var textCount : TextView = viewGroup.getChildAt(2) as TextView
        textNum.text = (current + 1).toString()
        textCount.text = count.toString()
    }
}