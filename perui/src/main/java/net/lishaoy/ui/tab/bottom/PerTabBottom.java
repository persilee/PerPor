package net.lishaoy.ui.tab.bottom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import net.lishaoy.ui.tab.common.IPerTab;

public class PerTabBottom extends RelativeLayout implements IPerTab<PerTabBottomInfo<?>> {

    public PerTabBottom(Context context) {
        super(context);
    }

    public PerTabBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PerTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPerTabInfo(@NonNull PerTabBottomInfo<?> data) {

    }

    @Override
    public void resetHeight(int height) {

    }

    @Override
    public void onTabSelectedChange(int index, @NonNull PerTabBottomInfo<?> prevInfo, @NonNull PerTabBottomInfo<?> nextInfo) {

    }
}
