package net.lishaoy.ui.banner;

import android.content.Context;
import android.widget.Scroller;

public class PerBannerScroller extends Scroller {

    private int duration = 1000;

    public PerBannerScroller(Context context, int duration) {
        super(context);
        this.duration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, duration);
    }
}
