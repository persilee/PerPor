package net.lishaoy.perpro.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import net.lishaoy.common.ui.PerBaseFragment
import net.lishaoy.perpro.R
import net.lishaoy.perpro.fragment.home.HomeViewModel
import net.lishaoy.perpro.model.TabCategory
import net.lishaoy.ui.tab.bottom.PerTabBottomLayout
import net.lishaoy.ui.tab.common.IPerTabLayout
import net.lishaoy.ui.tab.top.PerTabTopInfo

class HomeFragment : PerBaseFragment() {
    private var topTabSelectIndex: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PerTabBottomLayout.clipBottomPadding(hone_view_pager)
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)
        viewModel.queryTabList().observe(viewLifecycleOwner, Observer {
            it?.let {
                updateUI(it)
            }
        })
    }

    private val onTabSelectedListener = IPerTabLayout.OnTabSelectedListener<PerTabTopInfo<*>> { index, _, _ ->
        if (hone_view_pager.currentItem != index) {
            hone_view_pager.setCurrentItem(index, false)
        }
    }

    private fun updateUI(data: List<TabCategory>) {
        if (!isAlive) return
        val viewPager = hone_view_pager
        val tabTopLayout = tab_top_layout
        val topTabs = mutableListOf<PerTabTopInfo<Int>>()
        data.forEachIndexed { index, tabCategory ->
            val defaultColor = ContextCompat.getColor(context!!, R.color.color_333)
            val selectColor = ContextCompat.getColor(context!!, R.color.color_dd2)
            val tabTopInfo = PerTabTopInfo<Int>(tabCategory.categoryName, defaultColor, selectColor)
            topTabs.add(tabTopInfo)
        }
        tabTopLayout.inflateInfo(topTabs as List<PerTabTopInfo<*>>)
        tabTopLayout.defaultSelected(topTabs[0])
        tabTopLayout.addTabSelectedChangeListener(onTabSelectedListener)

        val adapter = if (viewPager.adapter == null) {
            val homePagerAdapter = HomePagerAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )
            viewPager.adapter = homePagerAdapter
            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    if (position != topTabSelectIndex) {
                        tabTopLayout.defaultSelected(topTabs[position])
                        topTabSelectIndex = position
                    }
                }
            })
            homePagerAdapter
        } else {
            viewPager.adapter as HomePagerAdapter
        }

        adapter.update(data)
    }

    inner class HomePagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        val tabs = mutableListOf<TabCategory>()
        val fragments = SparseArray<Fragment>(tabs.size)

        override fun getItem(position: Int): Fragment {
            val categoryId = tabs[position].categoryId
            val categoryKey = categoryId.toInt()
            var fragment = fragments.get(categoryKey, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(categoryId)
                fragments.put(categoryKey, fragment)
            }

            return fragment
        }

        override fun getItemPosition(`object`: Any): Int {
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val fragment = getItem(indexOfValue)
            return if (fragment == `object`) PagerAdapter.POSITION_UNCHANGED else PagerAdapter.POSITION_NONE
        }

        override fun getItemId(position: Int): Long {
            return tabs[position].categoryId.toLong()
        }

        override fun getCount(): Int {
            return tabs.size
        }

        fun update(data: List<TabCategory>) {
            tabs.clear()
            tabs.addAll(data)
            notifyDataSetChanged()
        }
    }
}