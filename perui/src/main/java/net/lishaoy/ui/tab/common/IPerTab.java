package net.lishaoy.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public interface IPerTab<D> extends IPerTabLayout.OnTabSelectedListener<D> {
    void setPerTabInfo(@NonNull D data);

    void resetHeight(@Px int height);
}
