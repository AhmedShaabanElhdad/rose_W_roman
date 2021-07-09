package com.smartzone.horizon.ui.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.City
import com.smartzone.diva_wear.databinding.ActivityRegisterBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.dailogs.CityDialog
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.ui.register.RegisterViewModel
import com.smartzone.myapp.ui.register.User
import com.smartzone.myapp.utilis.SOCIAL_RES
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern


class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    val viewModel: RegisterViewModel by viewModel()
    lateinit var binding: ActivityRegisterBinding

    var selectCity: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        binding.showPassword = false

        observerUser()

        viewModel.getCity()
        binding.etCity.setOnClickListener {
            binding.etCity.setOnClickListener {
                viewModel.cities.value?.let {
                    CityDialog(this).show(it, false) {
                        selectCity = it
                        binding.etCity.setText(selectCity?.name)
                    }
                } ?: run {
                    viewModel.getCity()
                }
            }
        }
        binding.btnSignUp.setOnClickListener {
            if (validateInput()) {
                viewModel.signUP(
                    binding.etPhone.text.toString(), binding.etPassword.text.toString(), "",
                    binding.etName.text.toString(), selectCity?.id, ""
                )
//                viewModel.checkInput(
//                    binding.etName.text.toString(),
//                    binding.etPhone.text.toString()
//                )
            }
        }
        binding.tittlePress.setOnClickListener {
            finish()
        }
        binding.checkTemn.setOnClickListener {
            //todo add url for privacy policy
            var url = "https://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.imgGoBack.setOnClickListener {
            finish()
        }
        //observerUser()
        var data =
            intent.extras?.getSerializable(SOCIAL_RES) as User?
        if (data != null) {
            binding.etName.setText(data.name)
        }
    }

    private fun validateInput(): Boolean {
        if (binding.etName.text.isBlank()) {
            binding.etName.error = getString(R.string.field_required)
            return false
        }
        if (binding.etPhone.text.isBlank()) {
            binding.etPhone.error = getString(R.string.field_required)
            return false
        }
        if (binding.etPhone.text.isNotBlank() && !isValidPhoneNumber(binding.etPhone.text.toString())) {
            binding.etPhone.error = getString(R.string.enter_valid_phone)
            return false
        }
        if (binding.etPassword.text.isBlank()) {
            binding.etPassword.error = getString(R.string.field_required)
            return false
        }
        if (selectCity == null) {
            Toast.makeText(this, getString(R.string.mustSelectCity), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


    fun isValidPhoneNumber(
        phoneNumber: String
    ): Boolean {
        val regexEg = "^(010|011|012|015)[0-9]{8}$"
        return Pattern.matches(regexEg, phoneNumber)
    }

    fun observerUser() {
        viewModel.user.observe(this, Observer {
//            val registerData = User(
//                binding.etName.text.toString(),
//                binding.etPassword.text.toString(),
//                binding.etPhone.text.toString(),
//                selectCity?.id,
//                binding.etName.text.toString()
//            )

            SavePrefs(this, com.smartzone.myapp.data.pojo.User::class.java).save(it)
            MyApp.getApp().appPreferencesHelper.setLogin(true)
            startActivity(
                MainActivity.getIntent(this)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finishAffinity()
//            startActivity(VerficationActivity.getIntent(this).apply {
//                putExtra("data", registerData)
//            })
        })
    }


    companion object {
        fun getIntent(context: Context): Intent = Intent(context, RegisterActivity::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }
}