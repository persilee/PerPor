package net.lishaoy.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.lishaoy.library.util.PerDisplayUtil;

public abstract class PerOverView extends FrameLayout {

    public enum PerRefreshState {
        STATE_INIT, //初始态
        STATE_VISIBLE, //Header展示的状态
        STATE_OVER, //超出可刷新距离的状态
        STATE_REFRESH, //刷新中的状态
        STATE_OVER_RELEASE //超出刷新位置松开手后的状态
    }
    protected PerRefreshState refreshState = PerRefreshState.STATE_INIT;
    public int pullRefreshHeight;
    public float minDamp = 1.6f;
    public float maxDamp = 2.0f;
    public PerOverView(@NonNull Context context) {
        super(context);
        preInit();
    }

    public PerOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public PerOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    protected void preInit() {
        pullRefreshHeight = PerDisplayUtil.dp2px(86, getResources());
        init();
    }

    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    protected abstract void onVisible();

    public abstract void onOver();

    public abstract void onRefresh();

    public abstract void onRefreshFinish();

    public void setState(PerRefreshState state) {
        this.refreshState = state;
    }

    public PerRefreshState getState() {
        return refreshState;
    }

}
