package net.lishaoy.biz_order.address

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import net.lishaoy.biz_order.R
import net.lishaoy.library.util.PerRes
import net.lishaoy.ui.empty.EmptyView

@Module
@InstallIn(ActivityComponent::class)
object AddressModule {

    @Provides
    @ActivityScoped
    fun newEmptyView(@ActivityContext context: Context): EmptyView {
        val emptyView = EmptyView(context)
        emptyView.setBackgroundColor(Color.WHITE)
        emptyView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        emptyView.setIcon(R.string.list_empty)
        emptyView.setDesc(PerRes.getSting(R.string.list_empty_desc))
        return emptyView
    }

}