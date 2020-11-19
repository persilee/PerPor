package net.lishaoy.perpro.fragment

import android.os.Bundle
import net.lishaoy.common.ui.PerAbsListFragment

class HomeTabFragment: PerAbsListFragment() {

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

}