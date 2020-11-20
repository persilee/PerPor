package net.lishaoy.ui.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

open class PerViewHolder(val view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View?
        get() = view

}