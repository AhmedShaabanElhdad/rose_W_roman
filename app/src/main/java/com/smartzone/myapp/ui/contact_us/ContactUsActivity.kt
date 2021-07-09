package com.smartzone.myapp.ui.contact_us

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityContactUsBinding
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.AppUtils
import com.smartzone.myapp.utilis.SavePrefs
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactUsActivity : BaseActivity<ActivityContactUsBinding>() {
    lateinit var binding: ActivityContactUsBinding
    val viewModel: ContactusViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()

        SavePrefs(MyApp.getApp(), User::class.java).load()?.apply {
            binding.name.setText(this.name)
            binding.etPhone.setText(this.mobile)
        }



        binding.back.setOnClickListener {
            onBackPressed()
        }
        viewModel.getSetting()
        observeSetting()

        binding.sendBtn.setOnClickListener {
            if (validateInput()) {
                viewModel.sendFeadBack(
                    binding.name.toString(),
                    binding.etPhone.text.toString(),
                    binding.message.text.toString()
                )
            }
        }
        binding.instegram.setOnClickListener {
            val url = binding.instegram.tag as String
            startActivity(AppUtils.newInstagramProfileIntent(packageManager, url))
        }
        binding.whats.setOnClickListener {
            var url = binding.whats.tag as String
            url = "+2${url}"

            AppUtils.newWhatsIntent(packageManager, url)?.let {
                startActivity(it)
            } ?: run {
                Toast.makeText(this, "لايوجد واتس اب", Toast.LENGTH_LONG).show()
            }

        }
        binding.face.setOnClickListener {
            val url = binding.face.tag as String
            startActivity(AppUtils.newFacebookIntent(packageManager, url))
        }
    }

    private fun validateInput(): Boolean {
        if (binding.name.text.isBlank()) {
            binding.name.error = getString(R.string.field_required)
            return false
        }
        if (binding.etPhone.text.isBlank()) {
            binding.etPhone.error = getString(R.string.field_required)
            return false
        }
        if (binding.message.text.isBlank()) {
            binding.message.error = getString(R.string.field_required)
            return false
        }

        return true
    }


    fun observeSetting() {
        viewModel.setting.observe(this, Observer {
            binding.instegram.setTag(it.instagram)
            binding.face.setTag(it.facebook)
            binding.whats.setTag(it.whatsapp)
            binding.twitter.setTag(it.twitter)
        })
        viewModel.success.observe(this, Observer {
            if (it) {
                Toast.makeText(this, getString(R.string.yourRequest), Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_contact_us
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, ContactUsActivity::class.java)
    }
}