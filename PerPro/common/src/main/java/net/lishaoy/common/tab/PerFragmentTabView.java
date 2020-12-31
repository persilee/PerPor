package net.lishaoy.common.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerFragmentTabView extends FrameLayout {

    private PerTabViewAdapter adapter;
    private int currentPosition;

    public PerFragmentTabView(@NonNull Context context) {
        super(context);
    }

    public PerFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PerFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PerTabViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(PerTabViewAdapter adapter) {
        if (this.adapter != null || adapter == null) return;
        this.adapter = adapter;
        currentPosition = -1;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position >= adapter.getCount()) return;
        if (currentPosition != position) {
            currentPosition = position;
            adapter.instantiateItem(this, position);
        }
    }

    public Fragment getCurrentFragment() {
        if (this.adapter == null)
            throw new IllegalArgumentException("please call setAdapter first");
        return adapter.getCurFragment();
    }

}
