package net.lishaoy.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import net.lishaoy.library.util.PerDisplayUtil;
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
        autoScroll(info);
    }

    int tabWidth;

    private void autoScroll(PerTabTopInfo<?> info) {
        PerTabTop tabTop = findTab(info);
        if (tabTop == null) return;
        int index = infoList.indexOf(info);
        int[] loc = new int[2];
        int scrollWidth;
        tabTop.getLocationInWindow(loc); //获取一个控件在其父窗口中的坐标位置
        if (tabWidth == 0) tabWidth = tabTop.getWidth();
        //判断 TabTop 是否在屏幕的右边（loc[0]：获取横坐标的位置）
        if ((loc[0] + tabWidth / 2) > PerDisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
            scrollWidth = rangeScrollWidth(index, 2);
        } else {
            scrollWidth = rangeScrollWidth(index, -2);
        }
        scrollTo(getScrollX() + scrollWidth, 0);
    }

    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i < Math.abs(range); i++) {
            int next;
            if (range < 0) next = range + i + index;
            else next = range - i + index;
            if (next >= 0 && next < infoList.size()) {
                if (range < 0)
                    scrollWidth -= scrollWidth(next, false);
                else
                    scrollWidth += scrollWidth(next, true);
            }
        }
        return scrollWidth;
    }

    private int scrollWidth(int index, boolean toRight) {
        PerTabTop tabTop = findTab(infoList.get(index));
        if (tabTop == null) return 0;
        Rect rect = new Rect();
        tabTop.getLocalVisibleRect(rect);
        Log.d("TabTop", "scrollWidth: " + rect);
        if (toRight) {
            if (rect.right > tabWidth) return tabWidth;
            else return tabWidth - rect.right;
        } else {
            if (rect.left <= -tabWidth) return tabWidth;
            else if (rect.left > 0) return rect.left;
            else return 0;
        }
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
