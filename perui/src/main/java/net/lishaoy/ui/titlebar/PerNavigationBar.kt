package net.lishaoy.ui.titlebar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.*
import android.widget.Button
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.library.util.PerRes
import net.lishaoy.ui.R
import net.lishaoy.ui.iconfont.IconFontButton
import java.lang.IllegalStateException

class PerNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var navAttr: Attrs
    private val leftViewList = ArrayList<View>()
    private val rightViewList = ArrayList<View>()
    private val leftViewId = View.NO_ID
    private val rightViewId = View.NO_ID

    init {
        navAttr = parseNavAttrs(context, attrs, defStyleAttr)
    }

    fun addLeftTextButton(@StringRes stringRes: Int, viewId: Int): Button {
        return addLeftTextButton(PerRes.getSting(stringRes), viewId)
    }

    fun addLeftTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = viewId
        if (leftViewList.isEmpty()) {
            button.setPadding(navAttr.horPadding * 2, 0, navAttr.horPadding, 0)
        } else {
            button.setPadding(navAttr.horPadding, 0, navAttr.horPadding, 0)
        }

        addLeftView(button, generateTextButtonLayoutParams())

        return button
    }

    fun addLeftView(view: View, params: LayoutParams) {
        val viewId = view.id
        if (viewId == View.NO_ID) {
            throw IllegalStateException("left view must has an id.")
        }
        if (leftViewId == View.NO_ID) {
            params.addRule(ALIGN_PARENT_LEFT, viewId)
        } else {
            params.addRule(RIGHT_OF, leftViewId)
        }
    }

    private fun generateTextButtonLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    }

    private fun generateTextButton(): Button {
        val button = IconFontButton(context)
        button.setBackgroundColor(0)
        button.minWidth = 0
        button.minimumWidth = 0
        button.minWidth = 0
        button.minimumWidth = 0
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttr.btnTextSize)
        button.gravity = Gravity.CENTER
        return button
    }

    private fun parseNavAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int): Attrs {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.PerNavigationBar,
            defStyleAttr,
            R.style.navigationStyle
        )
        val icon = array.getString(R.styleable.PerNavigationBar_nav_icon)
        val iconColor = array.getColor(R.styleable.PerNavigationBar_nav_icon_color, Color.BLACK)
        val iconSize = array.getDimensionPixelSize(
            R.styleable.PerNavigationBar_nav_icon_size,
            PerDisplayUtil.dp2px(18f)
        )
        val title = array.getString(R.styleable.PerNavigationBar_nav_subtitle)
        val subTitle = array.getString(R.styleable.PerNavigationBar_nav_subtitle)
        val horPadding = array.getDimensionPixelSize(R.styleable.PerNavigationBar_hor_padding, 0)
        val btnTextSize = array.getDimensionPixelSize(
            R.styleable.PerNavigationBar_text_btn_text_size,
            PerDisplayUtil.dp2px(16f)
        )
        val btnTextColor = array.getColorStateList(R.styleable.PerNavigationBar_text_btn_text_color)
        val titleTextSize = array.getDimensionPixelSize(
            R.styleable.PerNavigationBar_title_text_size,
            PerDisplayUtil.dp2px(18f)
        )
        val titleTextSizeWithSubtitle = array.getDimensionPixelSize(
            R.styleable.PerNavigationBar_title_text_size_with_subTitle,
            PerDisplayUtil.dp2px(16f)
        )
        val titleTextColor = array.getColor(
            R.styleable.PerNavigationBar_title_text_color,
            PerRes.getColor(R.color.tap_selected_color)
        )
        val subTitleTextSize = array.getDimensionPixelSize(
            R.styleable.PerNavigationBar_subTitle_text_size,
            PerDisplayUtil.dp2px(14f)
        )
        val subTitleTextColor = array.getColor(
            R.styleable.PerNavigationBar_subTitle_text_color,
            PerRes.getColor(R.color.tap_selected_color)
        )
        val lineColor = array.getColor(
            R.styleable.PerNavigationBar_nav_line_color,
            PerRes.getColor(R.color.color_eee)
        )
        val lineHeight =
            array.getDimensionPixelOffset(R.styleable.PerNavigationBar_nav_line_height, 0)

        array.recycle()

        return Attrs(
            icon,
            iconColor,
            iconSize.toFloat(),
            title,
            subTitle,
            horPadding,
            btnTextSize.toFloat(),
            btnTextColor,
            titleTextSize.toFloat(),
            titleTextSizeWithSubtitle.toFloat(),
            titleTextColor,
            subTitleTextSize.toFloat(),
            subTitleTextColor,
            lineColor,
            lineHeight
        )
    }

    private data class Attrs(
        val navIconStr: String?,
        val navIconColor: Int,
        val navIconSize: Float,
        val navTitle: String?,
        val navSubtitle: String?,
        val horPadding: Int,
        val btnTextSize: Float,
        val btnTextColor: ColorStateList?,
        val titleTextSize: Float,
        val titleTextSizeWithSubTitle: Float,
        val titleTextColor: Int,
        val subTitleSize: Float,
        val subTitleTextColor: Int,
        val lineColor: Int,
        val lineHeight: Int
    )

}