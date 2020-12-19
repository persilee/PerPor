package net.lishaoy.ui.search

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.setPadding
import net.lishaoy.library.util.MainHandler
import net.lishaoy.ui.R
import net.lishaoy.ui.iconfont.IconFontTextView
import org.w3c.dom.Text
import java.lang.IllegalStateException

class PerSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        const val LEFT = 1
        const val CENTER = 0
        const val DEBOUNCE_TRIGGER_DURATION = 200L
    }

    private var simpleTextWatcher: SimpleTextWatcher? = null
    public var editText: EditText? = null
    private var searchIcon: IconFontTextView? = null
    private var hintTv: TextView? = null
    private var searchIconHintContainer: LinearLayout? = null
    private var clearIcon: IconFontTextView? = null
    private var keywordContainer: LinearLayout? = null
    private var keywordTv: TextView? = null
    private var keywordClearIcon: IconFontTextView? = null

    private val viewAttrs = AttrsParse.parseSearchViewAttrs(context, attrs, defStyleAttr)

    init {

        initEditText()
        initClearIcon()
        initSearchIconHintContainer()

        background = viewAttrs.searchBackground
        editText?.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val hasContent = s?.length ?: 0 > 0
                clearIcon?.visibility = if (hasContent) View.VISIBLE else View.GONE
                searchIconHintContainer?.visibility = if (hasContent) View.GONE else View.VISIBLE

                if (simpleTextWatcher != null) {
                    MainHandler.remove(debounceRunnable)
                    MainHandler.postDelay(DEBOUNCE_TRIGGER_DURATION, debounceRunnable)
                }
            }
        })
    }

    private val debounceRunnable = Runnable {
        if (simpleTextWatcher != null) {
            simpleTextWatcher!!.afterTextChanged(editText?.text)
        }
    }

    fun setDebounceTextChangedListener(simpleTextWatcher: SimpleTextWatcher) {
        this.simpleTextWatcher = simpleTextWatcher
    }

    fun setKeyword(keyword: String?, listener: OnClickListener) {
        ensureKeywordContainer()
        toggleSearchViewVisibility(true)
        editText?.text = null
        keywordTv?.text = keyword
        keywordClearIcon?.setOnClickListener {
            toggleSearchViewVisibility(false)
            listener.onClick(it)
        }
    }

    fun setClearIconClickListener(listener: OnClickListener) {
        clearIcon?.setOnClickListener {
            editText?.text = null
            clearIcon?.visibility = View.GONE
            searchIcon?.visibility = View.VISIBLE
            hintTv?.visibility = View.VISIBLE
            searchIconHintContainer?.visibility = View.VISIBLE

            listener.onClick(it)
        }
    }

    fun setHintText(hintText: String) {
        hintTv?.text = hintText
    }

    fun getKeyword(): String? {
        return keywordTv?.text.toString()
    }

    private fun toggleSearchViewVisibility(b: Boolean) {
        editText?.visibility = if (b) View.GONE else View.VISIBLE
        clearIcon?.visibility = View.GONE
        searchIconHintContainer?.visibility = if (b) View.GONE else View.VISIBLE
        searchIcon?.visibility = if (b) View.GONE else View.VISIBLE
        hintTv?.visibility = if (b) View.GONE else View.VISIBLE
        keywordContainer?.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun ensureKeywordContainer() {
        if (keywordContainer != null) return

        if (!TextUtils.isEmpty(viewAttrs.keywordClearIcon)) {
            keywordClearIcon = IconFontTextView(context, null)
            keywordClearIcon?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.keywordSize)
            keywordClearIcon?.setTextColor(viewAttrs.keywordColor)
            keywordClearIcon?.text = viewAttrs.keywordClearIcon
            keywordClearIcon?.id = R.id.id_search_keyword_clear_icon
            keywordClearIcon?.setPadding(
                viewAttrs.iconPadding,
                viewAttrs.iconPadding / 2,
                viewAttrs.iconPadding,
                viewAttrs.iconPadding / 2
            )
        }

        keywordTv = TextView(context)
        keywordTv?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.keywordSize)
        keywordTv?.setTextColor(viewAttrs.keywordColor)
        keywordTv?.includeFontPadding = false
        keywordTv?.isSingleLine = true
        keywordTv?.ellipsize = TextUtils.TruncateAt.END
        keywordTv?.filters = arrayOf(InputFilter.LengthFilter(viewAttrs.keywordMaxLen))
        keywordTv?.id = R.id.id_search_keyword_text_view
        keywordTv?.setPadding(
            viewAttrs.iconPadding,
            viewAttrs.iconPadding / 2,
            0,
            viewAttrs.iconPadding / 2
        )

        keywordContainer = LinearLayout(context)
        keywordContainer?.orientation = LinearLayout.HORIZONTAL
        keywordContainer?.gravity = Gravity.CENTER
        keywordContainer?.background = viewAttrs.keywordBackground
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        keywordContainer?.addView(keywordTv, params)
        if (keywordClearIcon != null) {
            keywordContainer?.addView(keywordClearIcon, params)
        }

        val kwParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        kwParams.addRule(CENTER_VERTICAL)
        kwParams.addRule(ALIGN_PARENT_LEFT)
        kwParams.leftMargin = viewAttrs.iconPadding
        kwParams.rightMargin = viewAttrs.iconPadding
        addView(keywordContainer, kwParams)
    }

    private fun initSearchIconHintContainer() {
        hintTv = TextView(context)
        hintTv?.setTextColor(viewAttrs.hintTextColor)
        hintTv?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.hintTextSize)
        hintTv?.isSingleLine = true
        hintTv?.text = viewAttrs.hintText
        hintTv?.id = R.id.id_search_hint_view

        searchIcon = IconFontTextView(context, null)
        searchIcon?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.searchIconSize)
        searchIcon?.setTextColor(viewAttrs.hintTextColor)
        searchIcon?.text = viewAttrs.searchIcon
        searchIcon?.setPadding(viewAttrs.iconPadding, 0, viewAttrs.iconPadding / 2, 0)

        searchIconHintContainer = LinearLayout(context)
        searchIconHintContainer?.orientation = LinearLayout.HORIZONTAL
        searchIconHintContainer?.gravity = Gravity.CENTER
        searchIconHintContainer?.addView(searchIcon)
        searchIconHintContainer?.addView(hintTv)

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_VERTICAL)
        when (viewAttrs.gravity) {
            CENTER -> params.addRule(CENTER_IN_PARENT)
            LEFT -> params.addRule(ALIGN_PARENT_LEFT)
            else -> throw IllegalStateException("not support gravity for now.")
        }

        addView(searchIconHintContainer, params)
    }

    private fun initClearIcon() {
        if (TextUtils.isEmpty(viewAttrs.clearIcon)) return
        clearIcon = IconFontTextView(context, null)
        clearIcon?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.clearIconSize)
        clearIcon?.text = viewAttrs.clearIcon
        clearIcon?.setTextColor(viewAttrs.searchTextColor)
        clearIcon?.setPadding(viewAttrs.iconPadding)

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_VERTICAL)
        params.addRule(ALIGN_PARENT_RIGHT)
        clearIcon?.layoutParams = params
        clearIcon?.visibility = View.GONE
        clearIcon?.id = R.id.id_search_clear_icon
        addView(clearIcon, params)
    }

    private fun initEditText() {
        editText = EditText(context)
        editText?.setTextColor(viewAttrs.searchTextColor)
        editText?.setBackgroundColor(Color.TRANSPARENT)
        editText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.searchTextSize)
        editText?.maxLines = 1
        editText?.setPadding(viewAttrs.iconPadding, 0, viewAttrs.iconPadding, 0)
        editText?.id = R.id.id_search_edit_view

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        params.addRule(CENTER_VERTICAL)
        addView(editText, params)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        MainHandler.remove(debounceRunnable)
    }

}