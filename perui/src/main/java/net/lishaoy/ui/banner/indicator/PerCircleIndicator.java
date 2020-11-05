package net.lishaoy.ui.banner.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.L;

import net.lishaoy.library.util.PerDisplayUtil;
import net.lishaoy.ui.R;

public class PerCircleIndicator extends FrameLayout implements PerIndicator<FrameLayout> {

    private @DrawableRes
    int pointNormal = R.drawable.shape_point_normal;
    private @DrawableRes
    int pointSelected = R.drawable.shape_point_select;
    private int pointHorizontalPadding;
    private int pointVerticalPadding;

    public PerCircleIndicator(@NonNull Context context) {
        this(context, null);
    }

    public PerCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PerCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {
        removeAllViews();
        if (count <= 0) return;
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView imageView;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins(pointHorizontalPadding, pointVerticalPadding, pointHorizontalPadding, pointVerticalPadding);
        for (int i = 0; i < count; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(params);
            if (i == 0) imageView.setImageResource(pointSelected);
            else imageView.setImageResource(pointNormal);
            layout.addView(imageView);
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER|Gravity.BOTTOM;
        addView(layout,layoutParams);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ImageView imageView = (ImageView) viewGroup.getChildAt(i);
            if (i == current) imageView.setImageResource(pointSelected);
            else imageView.setImageResource(pointNormal);
            imageView.requestLayout();
        }
    }

    private void init() {
        pointHorizontalPadding = PerDisplayUtil.dp2px(4, getContext().getResources());
        pointVerticalPadding = PerDisplayUtil.dp2px(12, getContext().getResources());
    }
}
