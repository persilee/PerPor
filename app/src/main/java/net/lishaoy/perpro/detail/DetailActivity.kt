package net.lishaoy.perpro.detail

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_detail.*
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.util.PerStatusBar
import net.lishaoy.perpro.R
import net.lishaoy.perpro.model.DetailModel
import net.lishaoy.perpro.model.GoodsModel
import net.lishaoy.ui.empty.EmptyView
import net.lishaoy.ui.item.PerAdapter

@Route(path = "/detail/main")
class DetailActivity : PerBaseActivity() {

    private lateinit var viewModel: DetailViewModel
    private var emptyView: EmptyView? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsModel: GoodsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        PerStatusBar.setStatusBar(this, true, Color.TRANSPARENT, true)
        PerRoute.inject(this)

        assert(!TextUtils.isEmpty(goodsId)) { "goodsId must not be null" }

        initView()
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it)
            }
        })

    }

    private fun bindData(detailModel: DetailModel) {



    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty3)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            emptyView!!.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            emptyView!!.setBackgroundColor(Color.WHITE)
            emptyView!!.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                viewModel.queryDetailData()
            })
            detail_container.addView(emptyView)
            detail_recycler_view.visibility = View.GONE
            emptyView!!.visibility = View.VISIBLE
        }
    }

    private fun initView() {
        detail_btn_back.setOnClickListener { onBackPressed() }

        detail_recycler_view.layoutManager = GridLayoutManager(this, 2)
        detail_recycler_view.adapter = PerAdapter(this)
    }
}