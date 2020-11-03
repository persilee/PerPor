package net.lishaoy.ui.refresh;

public interface PerRefresh {
    void refreshFinished();
    void setDisableRefreshScroll(boolean disableRefreshScroll);
    void setRefreshListener(PerRefreshListener refreshListener);
    void setRefreshOverView(PerOverView perOverView);
    interface PerRefreshListener {
        void onRefresh();
        boolean enableRefresh();
    }
}
