package com.smartzone.myapp.ui.change_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityChangePasswordBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {
    lateinit var binding: ActivityChangePasswordBinding
    val viewModel: ChangePasswordViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=getViewDataBinding()
        binding.back.setOnClickListener {
            onBackPressed()
        }
        viewModel.getSetting()
        observeSetting()

        binding.sendBtn.setOnClickListener {
            if (validateInput()) {
               viewModel.changePassword(binding.password.text.toString())
            }
        }
    }

    private fun validateInput(): Boolean {
        if (binding.password.text.isBlank()) {
            binding.password.error = getString(R.string.field_required)
            return false
        }
        if (binding.repassword.text.isBlank()) {
            binding.repassword.error = getString(R.string.field_required)
            return false
        }
        if (binding.password.text == binding.repassword.text) {
            binding.repassword.error = getString(R.string.missmatch)
            return false
        }

        return true
    }


    fun observeSetting(){
        viewModel.success.observe(this, Observer {
            if (it){
                Toast.makeText(this,getString(R.string.yourRequest),Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object{
        fun getIntent(context: Context)=Intent(context, ChangePasswordActivity::class.java)
    }
}