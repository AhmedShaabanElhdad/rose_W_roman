package com.smartzone.myapp.ui.main.cart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.Product
import com.smartzone.diva_wear.databinding.FragmentCartBinding
import com.smartzone.myapp.ui.base.BaseFragment
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.utilis.CartManger
import com.smartzone.myapp.ui.confirm_order.ConfirmOrderDetailsViewModel
import com.smartzone.myapp.ui.order_details.address.OrderAddressActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : BaseFragment<FragmentCartBinding>() {


    lateinit var cart: CartManger
    lateinit var binding: FragmentCartBinding

    val viewModel: ConfirmOrderDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        binding.recycleCategories.layoutManager = LinearLayoutManager(activity)
        cart = MyApp.getCart()
        binding.price.text = "${cart.calculatePrice()}"
        binding.notification.setOnClickListener {
            (activity as MainActivity).openNotification()
        }
        cart.orderBean.delviry?.apply {
            shipping = this.price.toFloat()
            binding.shippingPrice.text = this.price
        }

        val myList = cart.getCartList()
        if (myList.size > 0) {
            binding.empty.visibility = View.GONE
            binding.cartContent.visibility = View.VISIBLE
        }

        binding.recycleCategories.adapter = CartAdapter(myList, { deleteProduct ->
            clickDelete(product = deleteProduct)
        }, clickPlus = { product: Product, position: Int ->
            clickPlus(product, position)
        }, clickMinus = { product: Product, position: Int ->
            clickMinus(product, position)
        })
        binding.doneBuy.setOnClickListener {
            if (!cart.IsEmpty()) {
                cart.save()
                startActivity(OrderAddressActivity.getIntent(binding.getRoot().context, promocode))
            } else {
                Toast.makeText(activity, getString(R.string.thereIsNoProduct), Toast.LENGTH_LONG)
                    .show()
            }

        }

        binding.btnConfirm.setOnClickListener {
            val code = binding.editPromocode.text.toString()
            if (code.isNotEmpty() && cart.getCartCount() > 0)
                viewModel.checkPromoCode(
                    binding.`editPromocode`.text.toString(),
                    cart.calculatePrice().toString()
                )
            else
                binding.editPromocode.error = getString(R.string.field_required)
        }

        binding.totalPrice.text = "${calculateTotalPrice()}"

    }

    var promocode = ""
    var discount = 0.0f
    var shipping = 0.0f
    var tax = 0.0f
    fun observeSuccces() {
        viewModel.checkPromo.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                promocode = binding.editPromocode.text.toString()
                if (it.type.equals("percent")) {
                    discount = it.discount.toFloat()
                    binding.discount.text = it.discount
                } else {
                    shipping = 0.0f
                    binding.shippingPrice.text = "0.0"
                }
            } else {
                Toast.makeText(activity, it.message_ar, Toast.LENGTH_LONG).show()
                promocode = ""
                discount = 0.0f
                binding.discount.text = "0.0"
            }
            binding.totalPrice.text = "${calculateTotalPrice()}"
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeSuccces()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cart
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private fun clickMinus(product: Product, position: Int) {
        cart.removeProduct(product)
        binding.recycleCategories.adapter?.notifyItemChanged(position)
        binding.totalPrice.text = "${calculateTotalPrice()}"
    }

    private fun clickPlus(product: Product, position: Int) {
        cart.addproduct(product)
        binding.recycleCategories.adapter?.notifyItemChanged(position)
        binding.totalPrice.text = "${calculateTotalPrice()}"
    }

    private fun clickDelete(product: Product) {
        cart.removeFromCart(product)
        binding.recycleCategories.adapter?.notifyDataSetChanged()
        binding.totalPrice.text = "${calculateTotalPrice()}"
    }

    fun calculateTotalPrice(): Float {
        var total = 0.0f
        val totalPrice = cart.calculatePrice()
        tax = totalPrice * BuildConfig.TAX.toFloat()
        binding.tax.text = "$tax"
        binding.price.text = "$totalPrice"
        total = shipping - discount + totalPrice + tax
        return total
    }
}