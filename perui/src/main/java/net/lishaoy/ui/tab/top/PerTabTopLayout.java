package net.lishaoy.ui.tab.top;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import net.lishaoy.ui.tab.common.IPerTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PerTabTopLayout extends HorizontalScrollView implements IPerTabLayout<PerTabTop, PerTabTopInfo<?>> {

    private List<OnTabSelectedListener<PerTabTopInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private PerTabTopInfo<?> selectedInfo;
    private List<PerTabTopInfo<?>> infoList;

    public PerTabTopLayout(Context context) {
        this(context, null);
    }

    public PerTabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PerTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public PerTabTop findTab(@NonNull PerTabTopInfo<?> data) {
        ViewGroup viewGroup = getRootLayout(false);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof PerTabTop) {
                PerTabTop tabTop = (PerTabTop) view;
                if (tabTop.getTabInfo() == data) {
                    return tabTop;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<PerTabTopInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull PerTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<PerTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) return;
        this.infoList = infoList;
        LinearLayout layout = getRootLayout(true);
        selectedInfo = null;
        Iterator<OnTabSelectedListener<PerTabTopInfo<?>>> iterable = tabSelectedListeners.iterator();
        while (iterable.hasNext()) {
            if (iterable.next() instanceof PerTabTop) {
                iterable.remove();
            }
        }
        for (int i = 0; i < infoList.size(); i++) {
            final PerTabTopInfo<?> topInfo = infoList.get(i);
            PerTabTop tabTop = new PerTabTop(getContext());
            tabSelectedListeners.add(tabTop);
            tabTop.setPerTabInfo(topInfo);
            layout.addView(tabTop);
            tabTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected(topInfo);
                }
            });
        }
    }

    private void onSelected(PerTabTopInfo<?> info) {
        for (OnTabSelectedListener<PerTabTopInfo<?>> listener : tabSelectedListeners) {
            listener.onTabSelectedChange(infoList.indexOf(info), selectedInfo, info);
        }
        this.selectedInfo = info;
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout layout = (LinearLayout) getChildAt(0);
        if (layout == null) {
            layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addView(layout, params);
        } else if (clear) {
            layout.removeAllViews();
        }
        return layout;
    }
}
