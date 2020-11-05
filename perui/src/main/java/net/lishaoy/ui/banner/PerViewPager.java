package net.lishaoy.ui.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class PerViewPager extends ViewPager {

    private int intervalTime;
    private boolean autoPlay = true;
    private boolean isLayout;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            next();
            handler.postDelayed(this, intervalTime);
        }
    };

    public PerViewPager(@NonNull Context context) {
        super(context);
    }

    public PerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void setScrollDuration(int duration) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new PerBannerScroller(getContext(), duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        if (!autoPlay) handler.removeCallbacks(runnable);
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();
                break;
            default:
                stop();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isLayout && getAdapter() != null && getAdapter().getCount() > 0) {
            try {
                Field firstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
                firstLayout.setAccessible(true);
                firstLayout.set(this, false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        start();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (((Activity)getContext()).isFinishing()) super.onDetachedFromWindow();
        stop();
    }

    public void start() {
        handler.removeCallbacksAndMessages(null);
        if (autoPlay) handler.postDelayed(runnable, intervalTime);
    }

    private int next() {
        int nextPosition = -1;
        if (getAdapter() == null || getAdapter().getCount() <= 1) {
            stop();
            return nextPosition;
        }
        nextPosition = getCurrentItem() + 1;
        if (nextPosition >= getAdapter().getCount()) {
            nextPosition = ((PerBannerAdapter) getAdapter()).getFirstItem();
        }
        setCurrentItem(nextPosition, true);

        return nextPosition;
    }


}
