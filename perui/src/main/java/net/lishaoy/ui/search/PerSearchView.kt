package net.lishaoy.ui.search

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class PerSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        const val LEFT = 1
        const val CENTER = 0
        const val DEBOUNCE_TRIGGER_DURATION = 200L
    }

}