package net.lishaoy.ui.titlebar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.library.util.PerRes
import net.lishaoy.ui.R
import net.lishaoy.ui.iconfont.IconFontButton
import net.lishaoy.ui.iconfont.IconFontTextView
import java.lang.IllegalStateException

class PerNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var navAttr: Attrs
    private val leftViewList = ArrayList<View>()
    private val rightViewList = ArrayList<View>()
    private var leftViewId = View.NO_ID
    private var rightViewId = View.NO_ID
    private var titleView: IconFontTextView? = null
    private var subTitleView: IconFontTextView? = null
    private var titleContainer: LinearLayout? = null

    init {
        navAttr = parseNavAttrs(context, attrs, defStyleAttr)
        if (!TextUtils.isEmpty(navAttr.navTitle)) {
            setTitle(navAttr.navTitle!!)
        }
        if (!TextUtils.isEmpty(navAttr.navSubtitle)) {
            setSubTitle(navAttr.navSubtitle!!)
        }
        if (navAttr.lineHeight > 0) {
            addLineView()
        }
    }

    fun setCenterView(view: View) {
        var params = view.layoutParams
        if (params == null) {
            params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else if (params !is LayoutParams) {
            params = LayoutParams(params)
        }

        val centerViewParams = params as LayoutParams
        centerViewParams.addRule(RIGHT_OF, leftViewId)
        centerViewParams.addRule(LEFT_OF, rightViewId)
        params.addRule(CENTER_VERTICAL)
        addView(view, centerViewParams)
    }

    fun setNavListener(listener: OnClickListener) {
        if (!TextUtils.isEmpty(navAttr.navIconStr)) {
            val navBackView =
                addLeftTextButton(navAttr.navIconStr!!, R.id.id_nav_left_back_view)
            navBackView.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttr.navIconSize)
            navBackView.setTextColor(navAttr.navIconColor)
            navBackView.setOnClickListener(listener)
        }
    }

    fun addLeftTextButton(@StringRes stringRes: Int, viewId: Int): Button {
        return addLeftTextButton(PerRes.getSting(stringRes), viewId)
    }

    fun addLeftTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = viewId
        button.setPadding(navAttr.horPadding, 0, navAttr.horPadding, 0)

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
        leftViewId = viewId
        params.alignWithParent = true
        leftViewList.add(view)
        addView(view, params)
    }

    fun addRightTextButton(@StringRes stringRes: Int, viewId: Int): Button {
        return addRightTextButton(PerRes.getSting(stringRes), viewId)
    }

    fun addRightTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = viewId
        button.setPadding(navAttr.horPadding, 0, navAttr.horPadding, 0)

        addRightView(button, generateTextButtonLayoutParams())

        return button
    }

    fun addRightView(view: View, params: LayoutParams) {
        val viewId = view.id
        if (viewId == View.NO_ID) {
            throw IllegalStateException("right view must has an id.")
        }
        if (rightViewId == View.NO_ID) {
            params.addRule(ALIGN_PARENT_RIGHT, viewId)
        } else {
            params.addRule(LEFT_OF, leftViewId)
        }
        rightViewId = viewId
        params.alignWithParent = true
        rightViewList.add(view)
        addView(view, params)
    }

    fun setTitle(title: String) {
        ensureTitleView()
        titleView?.text = title
        titleView?.visibility = if (TextUtils.isEmpty((title))) View.GONE else View.VISIBLE
    }

    fun setSubTitle(title: String) {
        ensureSubTitleView()
        updateTitleViewStyle()
        subTitleView?.text = title
        subTitleView?.visibility = if (TextUtils.isEmpty((title))) View.GONE else View.VISIBLE
    }

    private fun ensureTitleView() {
        if (titleView == null) {
            titleView = IconFontTextView(context, null)
            titleView?.apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttr.titleTextColor)

                updateTitleViewStyle()
                ensureTitleContainer()
                titleContainer?.addView(titleView, 0)
            }
        }
    }

    private fun ensureTitleContainer() {
        if (titleContainer == null) {
            titleContainer = LinearLayout(context)
            titleContainer?.apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER

                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
                params.addRule(RelativeLayout.CENTER_IN_PARENT)
                this@PerNavigationBar.addView(titleContainer, params)
            }
        }
    }

    private fun updateTitleViewStyle() {
        if (titleView != null) {
            if (subTitleView == null || TextUtils.isEmpty(subTitleView!!.text)) {
                titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttr.titleTextSize)
                titleView?.typeface = Typeface.DEFAULT_BOLD
            } else {
                titleView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    navAttr.titleTextSizeWithSubTitle
                )
                titleView?.typeface = Typeface.DEFAULT
            }
        }
    }

    private fun ensureSubTitleView() {
        if (subTitleView == null) {
            subTitleView = IconFontTextView(context, null)
            subTitleView?.apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttr.subTitleTextColor)
                textSize = navAttr.subTitleSize
                ensureTitleContainer()
                titleContainer?.addView(titleView)
            }
        }
    }

    private fun addLineView() {
        val view = View(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, navAttr.lineHeight)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        view.layoutParams = params
        view.setBackgroundColor(navAttr.lineColor)
        addView(view)
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
        button.setTextColor(navAttr.btnTextColor)
        button.gravity = Gravity.CENTER
        button.includeFontPadding = false
        return button
    }

    private fun parseNavAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int): Attrs {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.perNavigationStyle, value, true)
        val defStyleRes = if (value.resourceId != 0) value.resourceId else R.style.navigationStyle
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.PerNavigationBar,
            defStyleAttr,
            defStyleRes
        )
        val icon = array.getString(R.styleable.PerNavigationBar_nav_icon)
        val iconColor = array.getColor(R.styleable.PerNavigationBar_nav_icon_color, Color.BLACK)
        val iconSize = array.getDimensionPixelSize(
            R.styleable.PerNavigationBar_nav_icon_size,
            PerDisplayUtil.dp2px(18f)
        )
        val title = array.getString(R.styleable.PerNavigationBar_nav_title)
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
            PerRes.getColor(R.color.normal_color)
        )
        val subTitleTextSize = array.getDimensionPixelSize(
            R.styleable.PerNavigationBar_subTitle_text_size,
            PerDisplayUtil.dp2px(14f)
        )
        val subTitleTextColor = array.getColor(
            R.styleable.PerNavigationBar_subTitle_text_color,
            PerRes.getColor(R.color.normal_color)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (titleContainer != null) {
            var leftUseSpace = paddingLeft
            for (view in leftViewList) {
                leftUseSpace += view.measuredWidth
            }

            var rightUseSpace = paddingRight
            for (view in rightViewList) {
                rightUseSpace += view.measuredWidth
            }

            val titleContainerWidth = titleContainer!!.measuredWidth
            val remainingSpace = measuredWidth - Math.max(leftUseSpace, rightUseSpace) * 2
            if (remainingSpace < titleContainerWidth) {
                val size =
                    MeasureSpec.makeMeasureSpec(remainingSpace, MeasureSpec.EXACTLY)
                titleContainer!!.measure(size, heightMeasureSpec)
            }
        }
    }

}