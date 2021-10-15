package com.smartzone.myapp.ui.order_details.address

import android.R.attr.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityOrderAddressBinding
import com.smartzone.myapp.ui.MapsActivity
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.customView.MyRadioButton
import com.smartzone.myapp.ui.order_details.details.OrderDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class OrderAddressActivity : BaseActivity<ActivityOrderAddressBinding>() {

    lateinit var binding: ActivityOrderAddressBinding
    val viewModel: OrderAddressesViewModel by viewModel()
    var promocode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        viewModel.getAddresses()
        observeAddresses()

        promocode = intent.extras?.getString("promocode", "").toString()

        binding.location.setOnClickListener {
            startActivity(MapsActivity.getIntent(this,promocode))
//            startActivityForResult(MapsActivity.getIntent(this,promocode), 1)
        }
        binding.notification.setOnClickListener {
            openNotification()
        }
        binding.back.setOnClickListener { onBackPressed() }
        binding.confirm.setOnClickListener {
            if (address.isEmpty()) {
                Toast.makeText(
                    this, getString(
                        R.string.mustLocation
                    ), Toast.LENGTH_LONG
                ).show()
            } else {
                startActivity(OrderDetailsActivity.getIntent(this, promocode, address,lat,lon))
            }
        }
    }

    var address = ""
    var lat = ""
    var lon = ""

    //    val handler=object : Handler(Looper.getMainLooper()){
//        override fun handleMessage(msg: Message) {
//            when(msg.what){
//                1 -> {
//                    val bundle = msg.data
//                    address = bundle.getString("address", "")
//                    binding.location.text = bundle.getString("address")
//                }
//            }
//        }
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
//            val mLat=data.getDoubleExtra("lat", 0.0)
//            val mLon=data.getDoubleExtra("lon", 0.0)
//            AppUtils.getAddress(handler, this, mLat, mLon)
            viewModel.getAddresses()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order_address
    }

    fun observeAddresses() {
        viewModel.addresses.observe(this, Observer {
            binding.addresses.removeAllViews()
            for (address in it) {
                val radioButton: MyRadioButton =
                    LayoutInflater.from(this@OrderAddressActivity).inflate(
                        R.layout.item_address,
                        null
                    ) as MyRadioButton

                radioButton.saveAddress(address)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                params.setMargins(50, 10, 50, 10)
                params.layoutDirection = LayoutDirection.RTL
                radioButton.textSize = 12f
                radioButton.layoutParams = params
                radioButton.setPadding(50, 5, 50, 5)

                radioButton.setAddressNameWith(address.name)
                radioButton.setAddressDetailsWith(address.address)
                binding.addresses.addView(radioButton)
            }

            binding.addresses.setOnCheckedChangeListener { group, checkedId ->
                var myAddress = (findViewById<MyRadioButton>(checkedId)).getAddress()!!
                address = myAddress.address
                lat = myAddress.latitude
                lon = myAddress.longitude

            }

//                DeliveryCityAdapter(this, android.R.layout.simple_spinner_item, cities)
        })
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {

        fun getIntent(context: Context, promocode: String): Intent {
            val intent = Intent(context, OrderAddressActivity::class.java)
            intent.putExtra("promocode", promocode)
            return intent
        }
    }
}