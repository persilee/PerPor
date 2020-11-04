package net.lishaoy.ui.banner;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import net.lishaoy.ui.banner.indicator.PerIndicator;

import java.util.List;

public interface IPerBanner {

    void setBannerData(@LayoutRes int layoutResId, @NonNull List<? extends PerBannerMo> models);

    void setBannerData(@NonNull List<? extends PerBannerMo> models);

    void setPerIndicator(PerIndicator<?> indicator);

    void setAutoPlay(boolean autoPlay);

    void setLoop(boolean loop);

    void setIntervalTime(int intervalTime);

    void setBindAdapter(IBindAdapter bindAdapter);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setOnBannerClickListener(OnBannerClickListener onBannerClickListener);

    void setScrollDuration(int duration);

    interface OnBannerClickListener {
        void onBannerClick(@NonNull PerBannerAdapter.PerBannerViewHolder viewHolder, @NonNull PerBannerMo bannerMo, int position);
    }

}
