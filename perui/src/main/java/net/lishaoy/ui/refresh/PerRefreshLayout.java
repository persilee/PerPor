package net.lishaoy.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PerRefreshLayout extends FrameLayout implements PerRefresh {

    private PerOverView.PerRefreshState state;
    private GestureDetector gestureDetector;
    private PerRefreshListener listener;
    protected PerOverView overView;
    private int lastY;
    private boolean disableRefreshScroll;
    private AutoScroller scroller;

    public PerRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public PerRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PerRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void refreshFinished() {
        View header = getChildAt(0);
        overView.onRefreshFinish();
        overView.setState(PerOverView.PerRefreshState.STATE_INIT);
        int bottom = header.getBottom();
        if (bottom > 0) recover(bottom);
        state = PerOverView.PerRefreshState.STATE_INIT;
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void setRefreshListener(PerRefreshListener refreshListener) {
        this.listener = refreshListener;
    }

    @Override
    public void setRefreshOverView(PerOverView perOverView) {
        if (this.overView != null) removeView(overView);
        this.overView = perOverView;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(overView, 0, params);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View header = getChildAt(0);
        View child = getChildAt(1);
        if (header != null && child != null) {
            int childTop = child.getTop();
            if (state == PerOverView.PerRefreshState.STATE_REFRESH) {
                header.layout(0, overView.pullRefreshHeight - header.getMeasuredHeight(), right, overView.pullRefreshHeight);
                child.layout(0, overView.pullRefreshHeight, right, overView.pullRefreshHeight + child.getMeasuredHeight());
            } else {
                header.layout(0, childTop - header.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }
            View other;
            for (int i = 2; i < getChildCount(); ++i) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), perGestureDetector);
        scroller = new AutoScroller();
    }

    PerGestureDetector perGestureDetector = new PerGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float scrollX, float scrollY) {

            if (Math.abs(scrollX) > Math.abs(scrollY) || listener != null && !listener.enableRefresh())
                return false;
            if (disableRefreshScroll && state == PerOverView.PerRefreshState.STATE_REFRESH)
                return true;
            View header = getChildAt(0);
            View child = PerScrollUtil.findScrollableChild(PerRefreshLayout.this);
            if (PerScrollUtil.childScrolled(child)) return false;
            if ((state != PerOverView.PerRefreshState.STATE_REFRESH || header.getBottom() <= overView.pullRefreshHeight) && (header.getBottom() > 0 || scrollY <= 0.0f)) {
                if (state != PerOverView.PerRefreshState.STATE_OVER_RELEASE) {
                    int speed;
                    if (child.getTop() < overView.pullRefreshHeight) {
                        speed = (int) (lastY / overView.minDamp);
                    } else {
                        speed = (int) (lastY / overView.maxDamp);
                    }
                    boolean b = moveDown(speed, true);
                    lastY = (int) -scrollY;
                    return b;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    private boolean moveDown(int offsetY, boolean nonAuto) {
        View header = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {
            offsetY = -child.getTop();
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (state != PerOverView.PerRefreshState.STATE_REFRESH) {
                state = PerOverView.PerRefreshState.STATE_INIT;
            }
        } else if (state == PerOverView.PerRefreshState.STATE_REFRESH && childTop > overView.pullRefreshHeight) {
            return false;
        } else if (childTop <= overView.pullRefreshHeight) {
            if (overView.getState() != PerOverView.PerRefreshState.STATE_VISIBLE && nonAuto) {
                overView.onVisible();
                overView.setState(PerOverView.PerRefreshState.STATE_VISIBLE);
                state = PerOverView.PerRefreshState.STATE_VISIBLE;
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == overView.pullRefreshHeight && state == PerOverView.PerRefreshState.STATE_OVER_RELEASE) {
                refresh();
            }
        } else {
            if (overView.getState() != PerOverView.PerRefreshState.STATE_OVER && nonAuto) {
                overView.onOver();
                overView.setState(PerOverView.PerRefreshState.STATE_OVER);
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (overView != null) {
            overView.onScroll(header.getBottom(), overView.pullRefreshHeight);
        }
        return true;
    }

    private void refresh() {
        if (listener != null) {
            state = PerOverView.PerRefreshState.STATE_REFRESH;
            overView.onRefresh();
            overView.setState(PerOverView.PerRefreshState.STATE_REFRESH);
            listener.onRefresh();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!scroller.isFinished()) return false;
        View header = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            if (header.getBottom() > 0) {
                if (state != PerOverView.PerRefreshState.STATE_REFRESH) {
                    recover(header.getBottom());
                    return false;
                }
            }
            lastY = 0;
        }
        boolean consumed = gestureDetector.onTouchEvent(ev);
        if ((consumed || (state != PerOverView.PerRefreshState.STATE_INIT && state != PerOverView.PerRefreshState.STATE_REFRESH)) && header.getBottom() != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) return true;
        else return super.dispatchTouchEvent(ev);
    }

    private void recover(int bottom) {
        if (listener != null && bottom > overView.pullRefreshHeight) {
            scroller.recover(bottom - overView.pullRefreshHeight);
            state = PerOverView.PerRefreshState.STATE_OVER_RELEASE;
        } else {
            scroller.recover(bottom);
        }
    }

    private class AutoScroller implements Runnable {

        private Scroller scroller;
        private int lastY;
        private boolean isFinished;

        public AutoScroller() {
            scroller = new Scroller(getContext(), new LinearInterpolator());
            isFinished = true;
        }

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                moveDown(lastY - scroller.getCurrY(), false);
                lastY = scroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                isFinished = true;
            }
        }

        void recover(int dis) {
            if (dis < 0) return;
            removeCallbacks(this);
            lastY = 0;
            isFinished = false;
            scroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return isFinished;
        }
    }
}
