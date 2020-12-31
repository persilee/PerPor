package net.lishaoy.common.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.lishaoy.ui.tab.bottom.PerTabBottomInfo;

import java.util.List;

public class PerTabViewAdapter {

    private List<PerTabBottomInfo<?>> infoList;
    private Fragment curFragment;
    private FragmentManager fragmentManager;

    public PerTabViewAdapter(List<PerTabBottomInfo<?>> infoList, FragmentManager fragmentManager) {
        this.infoList = infoList;
        this.fragmentManager = fragmentManager;
    }

    public void instantiateItem(View container, int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (curFragment != null) transaction.hide(curFragment);
        String name = container.getId() + ":" + position;
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) transaction.add(container.getId(), fragment, name);
        }
        curFragment = fragment;
        transaction.commitNowAllowingStateLoss();
    }

    public List<PerTabBottomInfo<?>> getInfoList() {
        return infoList;
    }

    public int getCount() {
        return infoList == null ? 0 : infoList.size();
    }

    public Fragment getCurFragment() {
        return curFragment;
    }

    private Fragment getItem(int position) {

        try {
            return infoList.get(position).fragment.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
