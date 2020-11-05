package net.lishaoy.ui.banner;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import net.lishaoy.ui.R;
import net.lishaoy.ui.banner.indicator.PerCircleIndicator;
import net.lishaoy.ui.banner.indicator.PerIndicator;

import java.util.List;

public class PerBannerDelegate implements IPerBanner, ViewPager.OnPageChangeListener {

    private Context context;
    private PerBanner banner;
    private PerBannerAdapter adapter;
    private PerIndicator<?> indicator;
    private boolean autoPlay;
    private boolean loop;
    private List<? extends PerBannerMo> bannerMos;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private int intervalTime = 3600;
    private OnBannerClickListener bannerClickListener;
    private PerViewPager viewPager;
    private int scrollDuration = -1;

    public PerBannerDelegate(Context context, PerBanner banner) {
        this.context = context;
        this.banner = banner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends PerBannerMo> models) {
        bannerMos = models;
        init(layoutResId);
    }

    @Override
    public void setBannerData(@NonNull List<? extends PerBannerMo> models) {
        setBannerData(R.layout.per_banner_item, models);
    }

    @Override
    public void setPerIndicator(PerIndicator<?> indicator) {
        this.indicator = indicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        if (adapter != null) adapter.setAutoPlay(autoPlay);
        if (viewPager != null) viewPager.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) this.intervalTime = intervalTime;
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        adapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.pageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.bannerClickListener = onBannerClickListener;
    }

    @Override
    public void setScrollDuration(int duration) {
        this.scrollDuration = duration;
        if (viewPager != null && duration > 0) viewPager.setScrollDuration(duration);
    }

    private void init(int layoutResId) {
        if (adapter == null) adapter = new PerBannerAdapter(context);
        if (indicator == null) indicator = new PerCircleIndicator(context);
        indicator.onInflate(bannerMos.size());
        adapter.setLayoutResId(layoutResId);
        adapter.setBannerMos(bannerMos);
        adapter.setAutoPlay(autoPlay);
        adapter.setLoop(loop);
        adapter.setListener(bannerClickListener);
        viewPager = new PerViewPager(context);
        viewPager.setIntervalTime(intervalTime);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAutoPlay(autoPlay);
        viewPager.setAdapter(adapter);
        if (scrollDuration > 0) viewPager.setScrollDuration(scrollDuration);
        if ((loop || autoPlay) && adapter.getRealCount() != 0) {
            int firstItem = adapter.getFirstItem();
            viewPager.setCurrentItem(firstItem, false);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        banner.removeAllViews();
        banner.addView(viewPager, params);
        banner.addView(indicator.get(), params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (pageChangeListener != null && adapter.getRealCount() != 0) {
            pageChangeListener.onPageScrolled(position % adapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (adapter.getRealCount() == 0) return;
        position = position % adapter.getRealCount();
        if (pageChangeListener != null) pageChangeListener.onPageSelected(position);
        if (indicator != null) indicator.onPointChange(position, adapter.getRealCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (pageChangeListener != null) pageChangeListener.onPageScrollStateChanged(state);
    }
}
