package net.lishaoy.common.ui.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class IconFontTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = 0
): AppCompatTextView(context, attributeSet, defStyleAttr) {

    init {
        val typeface = Typeface.createFromAsset(context.assets, "/fonts/iconfont.ttf")
        setTypeface(typeface)
    }

}