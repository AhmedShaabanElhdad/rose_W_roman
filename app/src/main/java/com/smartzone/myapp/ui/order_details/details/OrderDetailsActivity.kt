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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
//        viewModel.getCity()
//        observeCity()

        promocode = intent.extras?.getString("promocode", "").toString()
        address = intent.extras?.getString("address", "").toString()


        binding.notification.setOnClickListener {
            openNotification()
        }
        binding.back.setOnClickListener { onBackPressed() }
        binding.confirm.setOnClickListener {
            startActivity(ConfirmOrderDetailsActvity.getIntent(this, promocode,address))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order_details
    }


    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {

        fun getIntent(context: Context, promocode: String, address: String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra("promocode", promocode)
            intent.putExtra("address", address)
            return intent
        }
    }
}