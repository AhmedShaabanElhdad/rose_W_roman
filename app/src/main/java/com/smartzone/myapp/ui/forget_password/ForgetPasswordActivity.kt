package com.smartzone.myapp.ui.forget_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityForgetPasswordBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgetPasswordActivity : BaseActivity<ActivityForgetPasswordBinding>() {

    private val viewModel: ForgetPassordViewModel by viewModel()
    lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        binding.btnSignUp.setOnClickListener {
            viewModel.forgetPassword(binding.etPhone.text.toString())
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }
        observeSuccess()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    fun observeSuccess() {
        viewModel.success.observe(this, Observer {
            Toast.makeText(this, getString(R.string.title_done), Toast.LENGTH_LONG).show()
        })
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, ForgetPasswordActivity::class.java)
    }
}