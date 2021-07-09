package com.smartzone.myapp.ui.congratlation_ordr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityDoneOrderBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.utilis.ID_KEY

class DoneOrderActivity : BaseActivity<ActivityDoneOrderBinding>() {
    lateinit var binding: ActivityDoneOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id by lazy {
            intent.extras?.getString(ID_KEY, "1")
        }

        binding=getViewDataBinding()
        binding.orderNum.text = id
        binding.goToHome.setOnClickListener {
            startActivity(MainActivity.getIntent(this).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                putExtra("booking", true)
            })
            finish()
        }
    }

    companion object{
        fun getIntent(context: Context):Intent= Intent(context, DoneOrderActivity::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_done_order
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }
}