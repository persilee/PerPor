package net.lishaoy.biz_order

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_order.*
import net.lishaoy.biz_order.address.AddEditingDialogFragment
import net.lishaoy.biz_order.address.Address
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.common.view.loadUrl
import net.lishaoy.library.util.PerRes
import net.lishaoy.library.util.PerStatusBar

@Route(path = "/order/main")
class OrderActivity : PerBaseActivity() {

    @JvmField
    @Autowired
    var shopName: String? = null

    @JvmField
    @Autowired
    var shopLogo: String? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsName: String? = null

    @JvmField
    @Autowired
    var goodsImage: String? = null

    @JvmField
    @Autowired
    var goodsPrice: String? = null

    private val viewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this, true, translucent = false)
        PerRoute.inject(this)
        setContentView(R.layout.activity_order)

        initView()
        updateTotalPayPrice(amount_view.getAmountValue())

        viewModel.queryAddress().observe(this, Observer {
            updateAddress(it)
        })
    }

    private fun updateAddress(address: Address?) {
        val hasAddress = address != null && !TextUtils.isEmpty(address.receiver)
        add_address.visibility = if (hasAddress) View.GONE else View.VISIBLE
        main_address.visibility = if (hasAddress) View.VISIBLE else View.GONE
        if (hasAddress) {
            user_name.text = address!!.receiver
            user_phone.text = address!!.phoneNum
            user_address.text = "${address.province} ${address.city} ${address.area}"
            main_address.setOnClickListener {
                showToast("to address list page")
            }
        } else {
            add_address.setOnClickListener {
                val dialogFragment = AddEditingDialogFragment.newInstance(null)
                dialogFragment.setSavedAddressListener(object : AddEditingDialogFragment.OnSavedAddressListener {
                    override fun onSaveAddress(address: Address?) {
                        updateAddress(address)
                    }
                })
                dialogFragment.show(supportFragmentManager, "add_address")
            }
        }
    }

    private fun initView() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        shopLogo?.apply { shop_logo.loadUrl(this) }
        shop_title.text = shopName
        goodsImage?.apply { goods_image.loadUrl(this) }
        goods_title.text = goodsName
        goods_price.text = goodsPrice

        amount_view.setAmountValueChangedListener {
            updateTotalPayPrice(it)
        }

        channel_wx_pay.setOnClickListener(channelPayListener)
        channel_ali_pay.setOnClickListener(channelPayListener)

        order_now.setOnClickListener {
            showToast("order yes")
        }
    }

    private fun updateTotalPayPrice(it: Int) {
        total_pay_price.text = String.format(
            PerRes.getSting(R.string.free_transport, PriceUtil.calculate(goodsPrice, it))
        )
    }

    private val channelPayListener = View.OnClickListener {
        val aliPayChecked = it.id == channel_ali_pay.id
        channel_ali_pay.isChecked = aliPayChecked
        channel_wx_pay.isChecked = !aliPayChecked
    }
}