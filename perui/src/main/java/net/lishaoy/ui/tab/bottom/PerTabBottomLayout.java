package net.lishaoy.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import net.lishaoy.library.util.PerDisplayUtil;
import net.lishaoy.library.util.PerViewUtil;
import net.lishaoy.ui.R;
import net.lishaoy.ui.tab.common.IPerTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PerTabBottomLayout extends FrameLayout implements IPerTabLayout<PerTabBottom, PerTabBottomInfo<?>> {
    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    private List<OnTabSelectedListener<PerTabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private PerTabBottomInfo<?> selectInfo;
    private float alpha = 1f;
    private static float tabBottomHeight = 50;
    private float bottomLineHeight = 1f;
    private String bottomLineColor = "#dfe0e1";
    private List<PerTabBottomInfo<?>> infoList;

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public static void setTabBottomHeight(float tabBottomHeight) {
        PerTabBottomLayout.tabBottomHeight = tabBottomHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    public PerTabBottomLayout(@NonNull Context context) {
        super(context);
    }

    public PerTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PerTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public PerTabBottom findTab(@NonNull PerTabBottomInfo<?> data) {
        ViewGroup viewGroup = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof PerTabBottom) {
                PerTabBottom tabBottom = (PerTabBottom) view;
                if (tabBottom.getTabInfo() == data) {
                    return tabBottom;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<PerTabBottomInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull PerTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<PerTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) return;
        this.infoList = infoList;
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        selectInfo = null;
        Iterator<OnTabSelectedListener<PerTabBottomInfo<?>>> iterable = tabSelectedListeners.iterator();
        while (iterable.hasNext()) {
            if (iterable.next() instanceof PerTabBottom) {
                iterable.remove();
            }
        }
        addBackground();
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setTag(TAG_TAB_BOTTOM);
        int width = PerDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        int height = PerDisplayUtil.dp2px(tabBottomHeight, getResources());
        for (int i = 0; i < infoList.size(); i++) {
            final PerTabBottomInfo<?> info = infoList.get(i);
            PerTabBottom tabBottom = new PerTabBottom(getContext());
            tabBottom.setPerTabInfo(info);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected(info);
                }
            });
            tabSelectedListeners.add(tabBottom);
            LayoutParams layoutParams = new LayoutParams(width, height);
            layoutParams.leftMargin = i * width;
            frameLayout.addView(tabBottom, layoutParams);
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(frameLayout, layoutParams);
        fixContentView();
    }

    private void addBottomLine() {
        View bottomLine = new View(getContext());
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));
        LayoutParams bottomLineParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PerDisplayUtil.dp2px(bottomLineHeight, getResources()));
        bottomLineParams.gravity = Gravity.BOTTOM;
        bottomLineParams.bottomMargin = PerDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLine, bottomLineParams);
        bottomLine.setAlpha(alpha);
    }

    private void onSelected(PerTabBottomInfo<?> info) {
        for (OnTabSelectedListener<PerTabBottomInfo<?>> listener : tabSelectedListeners) {
            listener.onTabSelectedChange(infoList.indexOf(info), selectInfo, info);
        }
        this.selectInfo = info;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.per_tab_bottom_layout, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, PerDisplayUtil.dp2px(tabBottomHeight, getResources()));
        layoutParams.gravity = Gravity.BOTTOM;
        addView(view, layoutParams);
        view.setAlpha(alpha);
    }

    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = PerViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = PerViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = PerViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, PerDisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);
        }
    }

    public static void clipBottomPadding(ViewGroup targetView) {
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, PerDisplayUtil.dp2px(tabBottomHeight));
            targetView.setClipToPadding(false);
        }
    }
}
