package net.lishaoy.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import net.lishaoy.ui.R;
import net.lishaoy.ui.banner.indicator.PerIndicator;

import java.util.List;

public class PerBanner extends FrameLayout implements IPerBanner {

    private PerBannerDelegate bannerDelegate;

    public PerBanner(@NonNull Context context) {
        this(context, null);
    }

    public PerBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PerBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bannerDelegate = new PerBannerDelegate(context, this);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PerBanner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.PerBanner_autoPlay, true);
        boolean loop = typedArray.getBoolean(R.styleable.PerBanner_loop, true);
        int intervalTime = typedArray.getInteger(R.styleable.PerBanner_intervalTime, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();

    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends PerBannerMo> models) {
        bannerDelegate.setBannerData(layoutResId, models);
    }

    @Override
    public void setBannerData(@NonNull List<? extends PerBannerMo> models) {
        bannerDelegate.setBannerData(models);
    }

    @Override
    public void setPerIndicator(PerIndicator<?> indicator) {
        bannerDelegate.setPerIndicator(indicator);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        bannerDelegate.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        bannerDelegate.setLoop(loop);
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        bannerDelegate.setIntervalTime(intervalTime);
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        bannerDelegate.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        bannerDelegate.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        bannerDelegate.setOnBannerClickListener(onBannerClickListener);
    }

    @Override
    public void setScrollDuration(int duration) {
        bannerDelegate.setScrollDuration(duration);
    }
}
