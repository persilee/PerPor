package net.lishaoy.biz_order.address

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_add_address.*
import net.lishaoy.biz_order.R
import net.lishaoy.library.util.PerRes

class AddEditingDialogFragment : AppCompatDialogFragment() {

    private var address: Address? = null
    private val viewModel by viewModels<AddressViewModel>()
    private var savedAddressListener: OnSavedAddressListener? = null

    companion object {
        const val KEY_ADDRESS_PARAMS = "key_address"

        fun newInstance(address: Address?): AddEditingDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_ADDRESS_PARAMS, address)
            val fragment = AddEditingDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = arguments?.getParcelable<Address>(KEY_ADDRESS_PARAMS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        val root = window?.findViewById(android.R.id.content) ?: container
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        return inflater.inflate(R.layout.dialog_add_address, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button =
            add_address_nav.addRightTextButton(R.string.if_close, R.id.nav_id_close)
        button.textSize = 25f
        button.setOnClickListener { dismiss() }

        add_address_pick.getEditText()
            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0)
        add_address_pick.getEditText().isFocusable = false
        add_address_pick.isFocusableInTouchMode = false
        add_address_pick.getEditText().setOnClickListener { }

        add_address_detail.getTitleView().gravity = Gravity.TOP
        add_address_detail.getEditText().gravity = Gravity.TOP
        add_address_detail.getEditText().isSingleLine = false

        if (address != null) {
            add_address_name.getEditText().setText(address!!.receiver)
            add_address_phone.getEditText().setText(address!!.phoneNum)
            add_address_pick.getEditText()
                .setText("${address!!.province} ${address!!.city} ${address!!.area}")
            add_address_detail.getEditText().setText(address!!.detail)
        }

        add_address_btn.setOnClickListener {
            saveAddress()
        }
    }

    private fun saveAddress() {
        val phone = add_address_phone.getEditText().text.toString().trim()
        val receiver = add_address_name.getEditText().text.toString().trim()
        val detail = add_address_detail.getEditText().text.toString().trim()
        val area = add_address_pick.getEditText().text.toString().trim()

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(receiver) || TextUtils.isEmpty(detail) || TextUtils.isEmpty(
                area
            )
        ) {
            Toast.makeText(
                context,
                PerRes.getSting(R.string.address_info_too_simple),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (address == null) {
            viewModel.saveAddress("", "", "", detail, receiver, phone)
                .observe(viewLifecycleOwner, observer)
        } else {
            viewModel.updateAddress(address!!.id, "", "", "", detail, receiver, phone)
                .observe(viewLifecycleOwner, observer)
        }
    }

    private val observer = Observer<Address?> {
        if (it != null) {
            savedAddressListener?.onSaveAddress(it)
            dismiss()
        }
    }

    fun setSavedAddressListener(listener: OnSavedAddressListener) {
        this.savedAddressListener = listener
    }

    interface OnSavedAddressListener {
        fun onSaveAddress(address: Address?)
    }

}