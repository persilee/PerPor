package net.lishaoy.perpro.logic;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import net.lishaoy.common.tab.PerFragmentTabView;
import net.lishaoy.common.tab.PerTabViewAdapter;
import net.lishaoy.perpro.R;
import net.lishaoy.perpro.fragment.CategoryFragment;
import net.lishaoy.perpro.fragment.FavoriteFragment;
import net.lishaoy.perpro.fragment.HomeFragment;
import net.lishaoy.perpro.fragment.ProfileFragment;
import net.lishaoy.perpro.fragment.RecommendFragment;
import net.lishaoy.ui.tab.bottom.PerTabBottomInfo;
import net.lishaoy.ui.tab.bottom.PerTabBottomLayout;
import net.lishaoy.ui.tab.common.IPerTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLogic {

    private PerFragmentTabView fragmentTabView;
    private PerTabBottomLayout tabBottomLayout;
    private List<PerTabBottomInfo<?>> infoList;
    private ActivityProvider activityProvider;
    private int currentItemIndex;
    private final static String SAVED_CURRENT_ID = "SAVED_CURRENT_ID";

    public PerFragmentTabView getFragmentTabView() {
        return fragmentTabView;
    }

    public PerTabBottomLayout getTabBottomLayout() {
        return tabBottomLayout;
    }

    public List<PerTabBottomInfo<?>> getInfoList() {
        return infoList;
    }

    public ActivityProvider getActivityProvider() {
        return activityProvider;
    }



    public interface ActivityProvider {
        <T extends View> T findViewById(@IdRes int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int resId);
    }

    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putInt(SAVED_CURRENT_ID, currentItemIndex);
    }

    public MainActivityLogic(ActivityProvider activityProvider, Bundle savedInstanceState) {
        this.activityProvider = activityProvider;
        if (savedInstanceState != null) currentItemIndex = savedInstanceState.getInt(SAVED_CURRENT_ID);
        initTabBottom();
    }

    private void initTabBottom() {
        tabBottomLayout = activityProvider.findViewById(R.id.tab_layout);
        tabBottomLayout.setAlpha(0.66f);
        infoList = new ArrayList<>();
        PerTabBottomInfo infoHome = new PerTabBottomInfo(
                "首页",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home),
                null,
                "#ff656667",
                "#ffd44949"
        );
        infoHome.fragment = HomeFragment.class;
        PerTabBottomInfo infoFavorite = new PerTabBottomInfo(
                "收藏",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite),
                null,
                "#ff656667",
                "#ffd44949"
        );
        infoFavorite.fragment = FavoriteFragment.class;
        PerTabBottomInfo infoCategory = new PerTabBottomInfo(
                "分类",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category),
                null,
                "#ff656667",
                "#ffd44949"
        );
        infoCategory.fragment = CategoryFragment.class;
        PerTabBottomInfo infoRecommend = new PerTabBottomInfo(
                "推荐",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend),
                null,
                "#ff656667",
                "#ffd44949"
        );
        infoRecommend.fragment = RecommendFragment.class;
        PerTabBottomInfo infoProfile = new PerTabBottomInfo(
                "我的",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile),
                null,
                "#ff656667",
                "#ffd44949"
        );
        infoProfile.fragment = ProfileFragment.class;
        infoList.add(infoHome);
        infoList.add(infoRecommend);
        infoList.add(infoCategory);
        infoList.add(infoFavorite);
        infoList.add(infoProfile);
        tabBottomLayout.inflateInfo(infoList);
        initFragmentTabView();
        tabBottomLayout.addTabSelectedChangeListener(new IPerTabLayout.OnTabSelectedListener<PerTabBottomInfo<?>>() {
            @Override
            public void onTabSelectedChange(int index, @Nullable PerTabBottomInfo<?> prevInfo, @NonNull PerTabBottomInfo<?> nextInfo) {
                fragmentTabView.setCurrentItem(index);
                MainActivityLogic.this.currentItemIndex = index;
            }
        });
        tabBottomLayout.defaultSelected(infoList.get(currentItemIndex));
    }

    private void initFragmentTabView() {
        PerTabViewAdapter tabViewAdapter = new PerTabViewAdapter(infoList, activityProvider.getSupportFragmentManager());
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        fragmentTabView.setAdapter(tabViewAdapter);
    }
}
