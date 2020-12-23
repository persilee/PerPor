package net.lishaoy.ui.selector

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.*
import android.widget.CheckedTextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.dialog_city_selector.*
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.library.util.PerRes
import net.lishaoy.ui.R
import net.lishaoy.ui.item.PerViewHolder
import net.lishaoy.ui.tab.top.PerTabTopInfo
import java.lang.IllegalStateException

class SelectorDialogFragment : AppCompatDialogFragment() {

    private var citySelectListener: SelectorDialogFragment.OnCitySelectListener? = null
    private var topTabSelectIndex: Int = 0
    private var dataSets: List<Province>? = null
    private lateinit var province: Province
    private val defaultColor = PerRes.getColor(R.color.color_333)
    private val selectColor = PerRes.getColor(R.color.color_dd2)
    private val pleasePickStr = getString(R.string.city_selector_tab_hint)

    companion object {

        private const val KEY_PARAMS_DATA_SET = "key_data_set"
        private const val KEY_PARAMS_DATA_SELECT = "key_data_select"
        private const val TAB_PROVINCE = 0
        private const val TAB_CITY = 1
        private const val TAB_DISTRICT = 2

        fun newInstance(province: Province?, list: List<Province>): SelectorDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_PARAMS_DATA_SELECT, province)
            args.putParcelableArrayList(KEY_PARAMS_DATA_SET, ArrayList(list))
            val fragment = SelectorDialogFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        val root = window?.findViewById<ViewGroup>(android.R.id.content) ?: container
        val contentView = inflater.inflate(R.layout.dialog_city_selector, root, false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (PerDisplayUtil.getDisplayHeightInPx(inflater.context) * 0.6).toInt()
        )
        window?.setGravity(Gravity.BOTTOM)

        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        city_close_btn.setOnClickListener { dismiss() }
        province = arguments?.getParcelable(KEY_PARAMS_DATA_SELECT) ?: Province()
        dataSets = arguments?.getParcelableArrayList(KEY_PARAMS_DATA_SET)
        requireNotNull(dataSets) { "params dataSets cannot be null" }

        refreshTabLayoutCount()

        city_tab_layout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            if (city_view_pager.currentItem != index) {
                city_view_pager.setCurrentItem(index, false)
            }
        }

        city_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position != topTabSelectIndex) {
                    city_tab_layout.defaultSelected(topTabs[position])
                    topTabSelectIndex = position
                }
            }
        })

        city_view_pager.adapter = CityPagerAdapter { tabIndex, selectDistrict ->
            when (selectDistrict.type) {
                TYPE_PROVINCE -> {
                    province = selectDistrict as Province
                }
                TYPE_CITY -> {
                    province.selectCity = selectDistrict as City
                }
                TYPE_DISTRICT -> {
                    province.selectDistrict = selectDistrict
                }
            }
            if (!TextUtils.equals(selectDistrict.type, TYPE_DISTRICT)) {
                refreshTabLayoutCount()
            } else {
                citySelectListener?.onCitySelect(province)
                dismiss()
            }
        }
    }

    private val topTabs = mutableListOf<PerTabTopInfo<Int>>()
    private fun refreshTabLayoutCount() {
        topTabs.clear()
        var addPleasePickTab = true

        if (!TextUtils.isEmpty(province.id)) {
            topTabs.add(newTab(province.districtName))
        }
        if (province.selectCity != null) {
            topTabs.add(newTab(province.selectCity!!.districtName))
        }
        if (province.selectDistrict != null) {
            topTabs.add(newTab(province.selectDistrict!!.districtName))
            addPleasePickTab = false
        }
        if (addPleasePickTab) {
            topTabs.add(newTab(pleasePickStr))
        }

        city_view_pager.adapter?.notifyDataSetChanged()
        city_tab_layout.post {
            city_tab_layout.inflateInfo(topTabs as List<PerTabTopInfo<*>>)
            city_tab_layout.defaultSelected(topTabs[if (addPleasePickTab) topTabs.size - 1 else 0])
        }
    }

    private fun newTab(districtName: String?): PerTabTopInfo<Int> {
        return PerTabTopInfo(districtName, defaultColor, selectColor)
    }

    inner class CityPagerAdapter(val itemClickCallback: (Int, District) -> Unit) : PagerAdapter() {

        private val listView = SparseArray<CityListView>(3)

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = listView[position] ?: CityListView(container.context)
            listView.put(position, view)
            val select: District?
            val list = when (position) {
                TAB_PROVINCE -> {
                    select = province
                    dataSets
                }
                TAB_CITY -> {
                    select = province.selectCity
                    province.cities
                }
                TAB_DISTRICT -> {
                    select = province.selectDistrict
                    province.selectCity!!.districts
                }
                else -> throw IllegalStateException("pageCount must be less than ${listView.size()}")
            }
            view.setData(select, list) {
                if (city_view_pager.currentItem != position) return@setData
                itemClickCallback(position, it)
            }

            if (view.parent != null) container.addView(view)

            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(listView[position])
        }

        override fun getItemPosition(`object`: Any): Int {
            return if (listView.indexOfValue(`object` as CityListView?) > 0) POSITION_NONE else POSITION_UNCHANGED
        }

        override fun getCount(): Int {
            return topTabs.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

    }

    inner class CityListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : RecyclerView(context, attrs, defStyleAttr) {

        private lateinit var itemClick: (District) -> Unit
        private var lastSelectDistrict: District? = null
        private val districtList = ArrayList<District>()
        private var lastSelectIndex = -1
        private var currentSelectIndex = -1

        fun setData(select: District?, list: List<District>?, onItemClick: (District) -> Unit) {
            if (list.isNullOrEmpty()) return
            lastSelectIndex = -1
            currentSelectIndex = -1
            itemClick = onItemClick
            districtList.clear()
            districtList.addAll(list)
            post { adapter?.notifyDataSetChanged() }
        }

        init {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = object : RecyclerView.Adapter<PerViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerViewHolder {
                    return PerViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.dialog_city_selector_list_item, parent, false)
                    )
                }

                override fun onBindViewHolder(holder: PerViewHolder, position: Int) {
                    val selected = holder.findViewById<CheckedTextView>(R.id.city_tab_title)
                    val district = districtList[position]
                    selected?.text = district.districtName
                    holder.itemView.setOnClickListener {
                        lastSelectDistrict = district
                        currentSelectIndex = position
                        notifyItemChanged(lastSelectIndex)
                        notifyItemChanged(position)
                    }
                    if (currentSelectIndex == position && currentSelectIndex != lastSelectIndex) {
                        itemClick(district)
                    }
                    if (lastSelectDistrict?.id == district.id) {
                        currentSelectIndex = position
                        lastSelectIndex = position
                    }
                    selected?.isChecked = currentSelectIndex == position
                }

                override fun getItemCount(): Int {
                    return districtList.size
                }

            }
        }

    }

    fun setCitySelectListener(listener: OnCitySelectListener) {
        citySelectListener = listener
    }

    interface OnCitySelectListener {
        fun onCitySelect(province: Province)
    }
}