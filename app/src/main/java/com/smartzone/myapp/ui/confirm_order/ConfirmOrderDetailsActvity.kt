package com.smartzone.myapp.ui.confirm_order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityConfirmOrderDetailsBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.congratlation_ordr.DoneOrderActivity
import com.smartzone.myapp.utilis.CartManger
import com.smartzone.myapp.utilis.ID_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmOrderDetailsActvity : BaseActivity<ActivityConfirmOrderDetailsBinding>() {

    val viewModel: ConfirmOrderDetailsViewModel by viewModel()
    lateinit var binding: ActivityConfirmOrderDetailsBinding
    lateinit var cart: CartManger
    var promocode = ""
    var address = ""
    var lon="0"
    var lat="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        binding.recycleProduct.layoutManager = LinearLayoutManager(this)
        cart = MyApp.getCart()
        binding.back.setOnClickListener { onBackPressed() }
        binding.recycleProduct.adapter = OrderAdapter(cart.getCartList())

        if (BuildConfig.TAX.toFloat()==0.0f)
            binding.taxlayout.visibility = View.GONE

        if (BuildConfig.FLAVOR == "horizon"){
            binding.etNote.visibility = View.VISIBLE
            binding.noteHeader.visibility = View.VISIBLE
        }

        binding.price.text = "${cart.calculatePrice()}"
        cart.orderBean.delviry?.apply {
            shipping = this.price.toFloat()
            binding.shippingPrice.text = this.price
        }
        binding.totalPrice.text = "${calculateTotalPrice()}"

        promocode = intent.extras?.getString("promocode", "").toString()
        address = intent.extras?.getString("address", "").toString()
        lat = intent.extras?.getString("lat", "0").toString()
        lon = intent.extras?.getString("lon", "0").toString()

//        binding.location.text = "${cart.orderBean.delviry?.name}"
        binding.location.text = address
        binding.confirmButton.setOnClickListener {
            viewModel.addCart(promocode,lat,lon,address,binding.etNote.text.toString())
        }

        if (promocode.isNotEmpty())
            viewModel.checkPromoCode(promocode, cart.calculatePrice().toString())

        observeSuccces()
    }

    fun observeSuccces() {
        viewModel.id.observe(this, Observer { id ->
                cart.clearOrder()
                startActivity(DoneOrderActivity.getIntent(this).apply {
                    putExtra(ID_KEY, id)
                })
        })
        viewModel.checkPromo.observe(this, Observer {
            if (it.status) {

                if (it.type.equals("percent")) {
                    promocode = it.id
                    binding.discount.text = it.discount
                } else {
                    shipping = 0.0f
                    binding.shippingPrice.text = "0.0"
                }

//                binding.btnConfirm.visibility = View.GONE
//                binding.editPromocode.isEnabled = false
//                promocode = binding.editPromocode.text.toString()

            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_confirm_order_details
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {
        fun getIntent(context: Context, promocode: String, address: String,latitude:String,longitude:String): Intent {
            val intent = Intent(context, ConfirmOrderDetailsActvity::class.java)
            intent.putExtra("promocode", promocode)
            intent.putExtra("address", address)
            intent.putExtra("lat", latitude)
            intent.putExtra("lon", longitude)
            return intent
        }
    }


    var promocodeId = "0"
    var discount = 0.0f
    var shipping = 0.0f
    var tax = 0.0f

    fun calculateTotalPrice(): Float {
        var total = 0.0f
        val totalPrice = cart.calculatePrice()
        tax = totalPrice * BuildConfig.TAX.toFloat()
        binding.tax.text = "$tax"
        binding.price.text = "$totalPrice"
        total =  totalPrice + tax + shipping - discount
        return total
    }

}