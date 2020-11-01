package net.lishaoy.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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

public class PerTabBottom extends RelativeLayout implements IPerTab<PerTabBottomInfo<?>> {

    private PerTabBottomInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabIcon;
    private TextView tabName;

    public PerTabBottomInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabIcon() {
        return tabIcon;
    }

    public TextView getTabName() {
        return tabName;
    }

    public PerTabBottom(Context context) {
        this(context, null);
        init();
    }

    public PerTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public PerTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.per_tab_bottom, this);
        tabImageView = findViewById(R.id.iv_image);
        tabIcon = findViewById(R.id.tv_icon);
        tabName = findViewById(R.id.tv_name);
    }

    @Override
    public void setPerTabInfo(@NonNull PerTabBottomInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    private void inflateInfo(boolean selected, boolean init) {
        if (tabInfo.tabType == PerTabBottomInfo.TabType.ICON) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabIcon.setVisibility(VISIBLE);
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
                tabIcon.setTypeface(typeface);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabName.setText(tabInfo.name);
                }
            }
            if (selected) {
                tabIcon.setText(TextUtils.isEmpty(tabInfo.selectedIconName) ? tabInfo.defaultIconName : tabInfo.selectedIconName);
                tabIcon.setTextColor(getTextColor(tabInfo.tintColor));
                tabName.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                tabIcon.setText(tabInfo.defaultIconName);
                tabIcon.setTextColor(getTextColor(tabInfo.defaultColor));
                tabName.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == PerTabBottomInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabIcon.setVisibility(GONE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabName.setText(tabInfo.name);
                }
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
    public void onTabSelectedChange(int index, @Nullable PerTabBottomInfo<?> prevInfo, @NonNull PerTabBottomInfo<?> nextInfo) {
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
