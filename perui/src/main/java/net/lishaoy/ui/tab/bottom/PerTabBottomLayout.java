package net.lishaoy.ui.tab.bottom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.lishaoy.library.util.PerDisplayUtil;
import net.lishaoy.ui.R;
import net.lishaoy.ui.tab.common.IPerTabLayout;

import java.util.ArrayList;
import java.util.List;

public class PerTabBottomLayout extends FrameLayout implements IPerTabLayout<PerTabBottom, PerTabBottomInfo<?>> {
    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    private List<OnTabSelectedListener<PerTabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private PerTabBottomInfo<?> selectInfo;
    private float alpha = 1f;
    private static float tabBottomHeight = 50;
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#dfe0e1";
    private List<PerTabBottomInfo<?>> tabBottomList;

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
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<PerTabBottomInfo<?>> listener) {

    }

    @Override
    public void defaultSelected(@NonNull PerTabBottomInfo<?> defaultInfo) {

    }

    @Override
    public void inflateInfo(@NonNull List<PerTabBottomInfo<?>> infoList) {
        if (tabBottomList.isEmpty()) return;
        this.tabBottomList = infoList;
        for (int i = getChildCount() - 1; i < 0; i--) {
            removeViewAt(i);
        }
        selectInfo = null;
        addBackground();
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setTag(TAG_TAB_BOTTOM);
        int width = PerDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        int height = PerDisplayUtil.dp2px(tabBottomHeight, getResources());
        for (int i = 0; i < infoList.size(); i++) {
            final PerTabBottomInfo<?> info = infoList.get(i);
            LayoutParams layoutParams = new LayoutParams(width, height);
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.leftMargin = i * width;
            PerTabBottom tabBottom = new PerTabBottom(getContext());
            tabBottom.setPerTabInfo(info);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected(info);
                }
            });
            tabSelectedListeners.add(tabBottom);
            frameLayout.addView(tabBottom, layoutParams);

        }
    }

    private void onSelected(PerTabBottomInfo<?> info) {
        for (OnTabSelectedListener<PerTabBottomInfo<?>> listener: tabSelectedListeners) {
            listener.onTabSelectedChange(tabBottomList.indexOf(info), selectInfo, info);
        }
        this.selectInfo = info;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.per_tab_bottom_layout, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PerDisplayUtil.dp2px(tabBottomHeight, getResources()));
        layoutParams.gravity = Gravity.BOTTOM;
        addView(view, layoutParams);
        view.setAlpha(alpha);
    }
}
