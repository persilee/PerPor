package net.lishaoy.library.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object PerRes {

    fun getSting(@StringRes id: Int): String {
        return context().getString(id)
    }

    fun getSting(@StringRes id: Int, vararg formatArgs: Any?): String {
        return context().getString(id, *formatArgs)
    }

    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context(), id)
    }

    fun getColorStateList(@ColorRes id: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context(), id)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context(), id)
    }

    private fun context(): Context {
        return AppGlobals.get() as Context
    }

}