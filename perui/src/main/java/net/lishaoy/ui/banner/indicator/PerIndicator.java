package net.lishaoy.ui.banner.indicator;

import android.view.View;

public interface PerIndicator<T extends View> {

    T get();
    void onInflate(int count);
    void onPointChange(int current, int count);

}
