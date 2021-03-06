package net.lishaoy.biz_detail

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_detail.*
import net.lishaoy.biz_detail.item.*
import net.lishaoy.biz_detail.model.DetailModel
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.util.PerStatusBar
import net.lishaoy.pub_mod.item.GoodsItem
import net.lishaoy.pub_mod.model.GoodsModel
import net.lishaoy.pub_mod.model.selectPrice
import net.lishaoy.service_login.LoginServiceProvider
import net.lishaoy.ui.empty.EmptyView
import net.lishaoy.ui.item.PerAdapter
import net.lishaoy.ui.item.PerDataItem

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
                preBindData()
                bindData(it as DetailModel)
            }
        })

    }

    private fun preBindData() {
        if (goodsModel == null) return
        val perAdapter = detail_recycler_view.adapter as PerAdapter
        perAdapter.addItem(
            0, HeaderItem(
                goodsModel!!.sliderImages,
                selectPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice),
                goodsModel!!.completedNumText,
                goodsModel!!.goodsName
            ), false
        )
    }

    private fun bindData(detailModel: DetailModel) {

        detail_recycler_view.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        val perAdapter = detail_recycler_view.adapter as PerAdapter
        val dataItems = mutableListOf<PerDataItem<*, *>>()
        dataItems.add(
            HeaderItem(
                detailModel.sliderImages,
                selectPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
        dataItems.add(CommentItem(detailModel))
        dataItems.add(ShopItem(detailModel))
        dataItems.add(GoodsAttrItem(detailModel))
        detailModel.gallery?.forEach {
            dataItems.add(GalleryItem(it))
        }
        detailModel.similarGoods?.let {
            dataItems.add(SimilarTitleItem())
            it.forEach {
                dataItems.add(GoodsItem(it, false))
            }
        }
        perAdapter.clearItems()
        perAdapter.addItems(dataItems, true)

        updateFavorite(detailModel.isFavorite)
        updateOrder(detailModel)
    }

    private fun updateOrder(detailModel: DetailModel) {
        detail_btn_order.text = selectPrice(
            detailModel.groupPrice,
            detailModel.marketPrice
        ) + getString(R.string.detail_order_text)

        detail_btn_order.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("shopName", detailModel.shop.name)
            bundle.putString("shopLogo", detailModel.shop.logo)
            bundle.putString("goodsId", detailModel.goodsId)
            bundle.putString("goodsImage", detailModel.sliderImage)
            bundle.putString("goodsName", detailModel.goodsName)
            bundle.putString(
                "goodsPrice",
                selectPrice(detailModel.groupPrice, detailModel.marketPrice)
            )
            PerRoute.startActivity(this, bundle, PerRoute.Destination.ORDER_MAIN)
        }
    }

    private fun updateFavorite(favorite: Boolean) {
        detail_btn_favorite.setOnClickListener {
            toggleFavorite()
        }
        detail_btn_favorite.setTextColor(
            ContextCompat.getColor(
                this,
                if (favorite) R.color.color_dd2 else R.color.color_999
            )
        )
    }

    private fun toggleFavorite() {
        if (!LoginServiceProvider.isLogin()) {
            LoginServiceProvider.login(this, Observer {
                if (it) {
                    toggleFavorite()
                }
            })
        } else {
            detail_btn_favorite.isClickable = false
            viewModel.toggleFavorite().observe(this, Observer {
                if (it != null) {
                    updateFavorite(it)
                    val message =
                        if (it) getString(R.string.detail_favorite_success) else getString(
                            R.string.detail_cancel_favorite_success
                        )
                    showToast(message)
                } else {

                }
                detail_btn_favorite.isClickable = true
            })
        }
    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty3)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            emptyView!!.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
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
        detail_recycler_view.addOnScrollListener(TitleScrollListener(callback = {
            detail_title_bar.setBackgroundColor(it)
        }))
    }
}