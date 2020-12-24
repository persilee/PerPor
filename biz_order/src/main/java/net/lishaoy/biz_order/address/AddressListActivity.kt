package net.lishaoy.biz_order.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_address_list.*
import net.lishaoy.biz_order.R
import net.lishaoy.common.route.RouteFlag
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.util.PerStatusBar
import net.lishaoy.ui.empty.EmptyView
import net.lishaoy.ui.item.PerAdapter
import javax.inject.Inject

@AndroidEntryPoint
@Route(path = "/address/list", extras = RouteFlag.FLAG_LOGIN)
class AddressListActivity : PerBaseActivity() {

    @Inject
    lateinit var emptyView: EmptyView
    private val viewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this,true,translucent = false)
        setContentView(R.layout.activity_address_list)

        initView()
        viewModel.queryAddressList().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                bindData(it)
            } else {
                showEmptyView(true)
            }
        })
    }

    private fun bindData(addressList: List<Address>) {
        val items = arrayListOf<AddressItem>()
        for (address in addressList) {
            items.add(newAddressItem(address))
        }
        val adapter = recycler_view.adapter as PerAdapter
        adapter.clearItems()
        adapter.addItems(items, true)
    }

    private fun newAddressItem(address: Address): AddressItem {
        return AddressItem(address, supportFragmentManager, removeItemCallback = {address, addressItem ->
            viewModel.deleteAddress(address.id).observe(this, Observer {
                if (it) {
                    addressItem.removeItem()
                }
            })
        }, itemClickCallback = {address ->
            val intent = Intent()
            intent.putExtra("result", address)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }, viewModel = viewModel)
    }

    private fun initView() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        nav_bar.addRightTextButton(R.string.nav_add_address, R.id.nav_id_add_address).setOnClickListener {
            showAddEditingDialog()
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = PerAdapter(this)
        recycler_view.adapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            showEmptyView(false)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            recycler_view.post {
                if (recycler_view.adapter!!.itemCount <= 0) {
                    showEmptyView(true)
                }
            }
        }
    }

    private fun showAddEditingDialog() {
        val addressDialog = AddEditingDialogFragment.newInstance(null)
        addressDialog.setSavedAddressListener(object : AddEditingDialogFragment.OnSavedAddressListener {
            override fun onSaveAddress(address: Address?) {
                val adapter: PerAdapter = recycler_view.adapter as PerAdapter
                adapter?.addItem(0, newAddressItem(address!!), true)
            }

        })
        addressDialog.show(supportFragmentManager, "add_address")

    }

    private fun showEmptyView(showEmptyView: Boolean) {
        recycler_view.isVisible = !showEmptyView
        emptyView.isVisible = showEmptyView
        if (emptyView.parent == null && showEmptyView) {
            address_list_layout.addView(emptyView)
        }
    }

    override fun onDestroy() {
        recycler_view?.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        super.onDestroy()
    }
}