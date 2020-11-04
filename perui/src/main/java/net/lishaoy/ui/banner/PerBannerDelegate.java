package net.lishaoy.ui.banner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import net.lishaoy.ui.banner.indicator.PerIndicator;

import java.util.List;

public class PerBannerDelegate implements IPerBanner {

    private Context context;
    private PerBanner banner;

    public PerBannerDelegate(Context context, PerBanner banner) {
        this.context = context;
        this.banner = banner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends PerBannerMo> models) {

    }

    @Override
    public void setBannerData(@NonNull List<? extends PerBannerMo> models) {

    }

    @Override
    public void setPerIndicator(PerIndicator<?> indicator) {

    }

    @Override
    public void setAutoPlay(boolean autoPlay) {

    }

    @Override
    public void setLoop(boolean loop) {

    }

    @Override
    public void setIntervalTime(int intervalTime) {

    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {

    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {

    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {

    }

    @Override
    public void setScrollDuration(int duration) {

    }
}
