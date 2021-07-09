package com.smartzone.myapp.ui.main.orders

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.reponse.Request
import com.smartzone.diva_wear.databinding.ActivityRequestDetailsBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.confirm_order.OrderAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestDetailsActivity : BaseActivity<ActivityRequestDetailsBinding>() {
    lateinit var binding: ActivityRequestDetailsBinding

    val viewModel: RequestsViewModel by viewModel()
    var idRequest:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.recycleProduct.layoutManager = LinearLayoutManager(this)
        idRequest=intent.extras?.getString("id",null)
        viewModel.getRequestById(idRequest)
        observeRequest()
        binding.notification.setOnClickListener {
            openNotification()
            finish()
        }
    }

    private fun observeRequest(){
        viewModel.request.observe(this, Observer {
            fillDataForRequest(it)
        })
    }

    private fun fillDataForRequest(request: Request) {
        request.products?.let {
            binding.recycleProduct.adapter = OrderAdapter(request.products)
        }
        binding.delivery.text = "${request.shipment} ${getString(R.string.currency)}"
        binding.total.text = "${request.price} ${getString(R.string.currency)}"
        val tax = request.price * BuildConfig.TAX.toFloat()
        binding.tax.text = "$tax ${getString(R.string.currency)}"
        binding.allPrice.text = "${request.price + request.shipment +tax} ${getString(R.string.currency)}"//"${request.price - request.shipment} ${getString(R.string.currency)}"
        binding.orderDate.text = request.created
        binding.orderNum.text = request.id
        setStatus(request.status.toInt())
    }

    private fun setStatus(status: Int) {
        when (status) {
            1 -> {
                binding.image1.setImageResource(R.drawable.checkmarkk)
//                binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY)

                binding.image2.setImageResource(R.drawable.ic_uncheck)
                binding.image3.setImageResource(R.drawable.ic_uncheck)
                binding.line2.setBackgroundColor(ContextCompat.getColor(this, R.color.white_grey))
            }
            2 -> {
                binding.image1.setImageResource(R.drawable.checkmarkk)
//                binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.image2.setImageResource(R.drawable.checkmarkk)
//                binding.image2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.image3.setImageResource(R.drawable.ic_uncheck)
            }
            3 -> {
                binding.image1.setImageResource(R.drawable.checkmarkk)
//                binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.image2.setImageResource(R.drawable.checkmarkk)
//                binding.image2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.image3.setImageResource(R.drawable.checkmarkk)
//                binding.image3.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, RequestDetailsActivity::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_request_details
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }
}