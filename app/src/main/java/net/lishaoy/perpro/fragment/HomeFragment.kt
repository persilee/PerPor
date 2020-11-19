package net.lishaoy.perpro.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import net.lishaoy.common.ui.PerBaseFragment
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.perpro.R
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.HomeApi
import net.lishaoy.perpro.model.TabCategory
import net.lishaoy.ui.tab.top.PerTabTopInfo

class HomeFragment : PerBaseFragment() {
    private var topTabSelectIndex: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queryTabList()
    }

    private fun queryTabList() {
        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : PerCallback<List<TabCategory>> {
                override fun onSuccess(response: PerResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        updateUI(data)
                    } else {
                        showToast("")
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun updateUI(data: List<TabCategory>) {
        if (!isAlive) return
        val viewPager = view_pager
        val tabTopLayout = tab_top_layout
        val topTabs = mutableListOf<PerTabTopInfo<Int>>()
        data.forEachIndexed { index, tabCategory ->
            val defaulColor = ContextCompat.getColor(context!!, R.color.color_333)
            val selectColor = ContextCompat.getColor(context!!, R.color.color_dd2)
            val tabTopInfo = PerTabTopInfo<Int>(tabCategory.categoryName, defaulColor, selectColor)
            topTabs.add(index, tabTopInfo)
        }
        tabTopLayout.inflateInfo(topTabs as List<PerTabTopInfo<*>>)
        tabTopLayout.defaultSelected(topTabs[0])
        tabTopLayout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            if (viewPager.currentItem != index) {
                viewPager.setCurrentItem(index, false)
            }
        }
        viewPager.adapter = HomePagerAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            data
        )

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position != topTabSelectIndex) {
                    tabTopLayout.defaultSelected(topTabs[position])
                    topTabSelectIndex = position
                }
            }
        })
    }

    inner class HomePagerAdapter(fm: FragmentManager, behavior: Int, val data: List<TabCategory>) :
        FragmentPagerAdapter(fm, behavior) {
        val fragments = SparseArray<Fragment>(data.size)
        override fun getItem(position: Int): Fragment {
            var fragment = fragments.get(position, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(data[position].categoryId)
                fragments.put(position, fragment)
            }

            return fragment
        }

        override fun getCount(): Int {
            return fragments.size()
        }

    }
}