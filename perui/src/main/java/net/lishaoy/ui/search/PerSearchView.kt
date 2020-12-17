package net.lishaoy.ui.search

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.setPadding
import net.lishaoy.ui.R
import net.lishaoy.ui.iconfont.IconFontTextView

class PerSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        const val LEFT = 1
        const val CENTER = 0
        const val DEBOUNCE_TRIGGER_DURATION = 200L
    }

    private var editText: EditText? = null
    private val searchIcon: IconFontTextView? = null
    private val hintTv: TextView? = null
    private val searchIconHintContainer: LinearLayout? = null
    private val clearIcon: IconFontTextView? = null

    val viewAttrs = AttrsParse.parseSearchViewAttrs(context, attrs, defStyleAttr)

    init {

        initEditText()
        initClearIcon()
        initSearchIconHintContainer()

    }

    private fun initSearchIconHintContainer() {

    }

    private fun initClearIcon() {
        if (TextUtils.isEmpty(viewAttrs.clearIcon)) return
        clearIcon?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.clearIconSize)
        clearIcon?.text = viewAttrs.clearIcon
        clearIcon?.setTextColor(viewAttrs.searchTextColor)
        clearIcon?.setPadding(viewAttrs.iconPadding)
    }

    private fun initEditText() {
        editText = EditText(context)
        editText?.setTextColor(viewAttrs.searchTextColor)
        editText?.setBackgroundColor(Color.TRANSPARENT)
        editText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.searchTextSize)
        editText?.setPadding(viewAttrs.iconPadding, 0, viewAttrs.iconPadding, 0)
        editText?.id = R.id.id_search_edit_view

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        params.addRule(CENTER_VERTICAL)
        addView(editText, params)
    }

}