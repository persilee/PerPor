package net.lishaoy.biz_order.address

import android.app.AlertDialog
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_address_list_item.*
import net.lishaoy.biz_order.R
import net.lishaoy.library.util.PerRes
import net.lishaoy.ui.item.PerDataItem
import net.lishaoy.ui.item.PerViewHolder

class AddressItem(
    var addressItem: Address,
    private val supportFragmentManager: FragmentManager,
    val removeItemCallback: (Address, AddressItem) -> Unit,
    val itemClickCallback: (Address) -> Unit,
    val viewModel: AddressViewModel
) : PerDataItem<Address, PerViewHolder>() {

    override fun onBindData(holder: PerViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.user_name.text = addressItem.receiver
        holder.user_phone.text = addressItem.phoneNum
        holder.user_address.text = "${addressItem.province} ${addressItem.city} ${addressItem.area}"
        holder.edit_address.setOnClickListener {
            val dialog = AddEditingDialogFragment.newInstance(addressItem)
            dialog.setSavedAddressListener(object :
                AddEditingDialogFragment.OnSavedAddressListener {
                override fun onSaveAddress(address: Address?) {
                    addressItem = address!!
                    refreshItem()
                }

            })
            dialog.show(supportFragmentManager, "edit_address")
        }
        holder.delete.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage(PerRes.getSting(R.string.address_delete_title))
                .setNegativeButton(R.string.address_delete_cancel, null)
                .setPositiveButton(R.string.address_delete_ensure) { dialog, which ->
                    dialog.dismiss()
                    removeItemCallback(addressItem, this)
                }
                .show()

        }
        holder.itemView.setOnClickListener {
            itemClickCallback(addressItem)
        }
        holder.default_address.setOnClickListener {
            viewModel.checkPosition = position
            viewModel.checkedAddressItem?.refreshItem()
            viewModel.checkedAddressItem = this
            refreshItem()
        }
        val select = viewModel.checkedAddressItem == this && viewModel.checkPosition == position
        holder.default_address.text =
            PerRes.getSting(if (select) R.string.address_default else R.string.set_default_address)
        holder.default_address.setTextColor(PerRes.getColor(if (select) R.color.color_dd2 else R.color.color_999))
        holder.default_address.setCompoundDrawablesWithIntrinsicBounds(
            if (select) R.drawable.ic_checked_red else 0, 0, 0, 0
        )

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.activity_address_list_item
    }

}