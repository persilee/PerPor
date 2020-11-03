package net.lishaoy.ui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.lishaoy.ui.R;
import net.lishaoy.ui.tab.common.IPerTab;

public class PerTabTop extends RelativeLayout implements IPerTab<PerTabTopInfo<?>> {

    private PerTabTopInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabName;
    private View indicator;

    public PerTabTopInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabName() {
        return tabName;
    }

    public PerTabTop(Context context) {
        this(context, null);
        init();
    }

    public PerTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public PerTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.per_tab_top, this);
        tabImageView = findViewById(R.id.iv_image);
        tabName = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setPerTabInfo(@NonNull PerTabTopInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    private void inflateInfo(boolean selected, boolean init) {
        if (tabInfo.tabType == PerTabTopInfo.TabType.TEXT) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabName.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabName.setText(tabInfo.name);
                }
            }
            if (selected) {
                tabName.setTextColor(getTextColor(tabInfo.tintColor));
                indicator.setVisibility(VISIBLE);
            } else {
                tabName.setTextColor(getTextColor(tabInfo.defaultColor));
                indicator.setVisibility(GONE);
            }
        } else if (tabInfo.tabType == PerTabTopInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabName.setVisibility(GONE);
            }
            if (selected) {
                tabImageView.setImageBitmap(tabInfo.selectedBitmap);
            } else {
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
            }
        }
    }

    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabName().setVisibility(View.GONE);
    }

    @Override
    public void onTabSelectedChange(int index, @Nullable PerTabTopInfo<?> prevInfo, @NonNull PerTabTopInfo<?> nextInfo) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) return;
        if (prevInfo == tabInfo)
            inflateInfo(false, false);
        else
            inflateInfo(true, false);
    }

    @ColorInt
    private int getTextColor(Object color) {
        if (color instanceof String)
            return Color.parseColor((String) color);
        else
            return (int) color;
    }
}
