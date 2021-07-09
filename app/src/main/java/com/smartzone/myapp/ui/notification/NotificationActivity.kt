package com.smartzone.myapp.ui.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityNotificationBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.orders.RequestDetailsActivity
import kotlinx.android.synthetic.main.activity_notification.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    lateinit var binding: ActivityNotificationBinding
    val viewModel: NotficationViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=getViewDataBinding()
        binding.empty.layoutManager=LinearLayoutManager(this)
        binding.toolbar.notification.visibility=View.GONE
        binding.toolbar.back.setOnClickListener {
            onBackPressed()
        }
        //binding.contentToolbar.tittle.text=getString(R.string.notification)
        viewModel.getNotifcations()
        observeNotification()
    }

    private fun observeNotification() {
        viewModel.notfications.observe(this, Observer {
            binding.empty.adapter= NotificationAdapter(it){
                notify->
                startActivity(RequestDetailsActivity.getIntent(this).apply {
                    putExtra("id",notify.invoice_id)
                })
            }
        })
    }

    companion object{
        fun getIntent(context: Context):Intent= Intent(context, NotificationActivity::class.java)
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_notification
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }
}