package net.lishaoy.perpro.detail

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import net.lishaoy.library.util.ColorUtil
import net.lishaoy.library.util.PerDisplayUtil
import kotlin.math.abs
import kotlin.math.min

class TitleScrollListener(val thresholdDp: Float = 100f, val callback: (Int) -> Unit) :
    RecyclerView.OnScrollListener() {

    private var lastFraction: Float = 0f
    val thresholdPx = PerDisplayUtil.dp2px(thresholdDp)

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val viewModel = recyclerView.findViewHolderForAdapterPosition(0) ?: return
        val top = abs(viewModel.itemView.top).toFloat()
        val fraction = top / thresholdPx
        if (lastFraction > 1f) {
            lastFraction = fraction
            return
        }
        val color = ColorUtil.getCurrentColor(Color.TRANSPARENT, Color.WHITE, min(fraction, 1f))
        callback(color)
        lastFraction = fraction
    }

}