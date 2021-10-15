package com.smartzone.myapp.ui.order_details.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.City
import com.smartzone.diva_wear.databinding.ActivityOrderDetailsBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.confirm_order.ConfirmOrderDetailsActvity
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderDetailsActivity : BaseActivity<ActivityOrderDetailsBinding>() {

    lateinit var binding: ActivityOrderDetailsBinding
    val viewModel: OrderDetailsViewModel by viewModel()
    var selectCity: City? = null
    var promocode = ""
    var address = ""
    var lon="0"
    var lat="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
//        viewModel.getCity()
//        observeCity()

        promocode = intent.extras?.getString("promocode", "").toString()
        address = intent.extras?.getString("address", "").toString()
        lat = intent.extras?.getString("lat", "0").toString()
        lon = intent.extras?.getString("lon", "0").toString()


        binding.notification.setOnClickListener {
            openNotification()
        }
        binding.back.setOnClickListener { onBackPressed() }
        binding.confirm.setOnClickListener {
            startActivity(ConfirmOrderDetailsActvity.getIntent(this, promocode,address,lat,lon))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order_details
    }


    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {

        fun getIntent(context: Context, promocode: String, address: String,latitude:String,longitude:String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra("promocode", promocode)
            intent.putExtra("address", address)
            intent.putExtra("lat", latitude)
            intent.putExtra("lon", longitude)
            return intent
        }
    }
}