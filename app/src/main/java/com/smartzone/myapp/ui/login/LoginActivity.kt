package com.smartzone.myapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.facebook.*
import com.google.android.gms.common.api.GoogleApiClient
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.User
import com.smartzone.diva_wear.databinding.ActivityLoginBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.forget_password.ForgetPasswordActivity
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.sa3d.ui.register.RegisterActivity
import com.smartzone.myapp.utilis.SOCIAL_RES
import com.smartzone.myapp.utilis.SavePrefs
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    val RC_GOOGLE_SIGN_IN = 102

    lateinit var mGoogleApiClient: GoogleApiClient
    lateinit var mFacebookCallbackManager: CallbackManager

    val viewModel: LoginViewModel by viewModel()
    lateinit var binding: ActivityLoginBinding
    var socialRes: com.smartzone.myapp.ui.register.User? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getViewDataBinding()
        binding.showPassword = false
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.etPhone.text.toString(), binding.etPassword.text.toString())
        }

        binding.btnVisitor.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        observerUser()


        binding.tittlePress.setOnClickListener {

            startActivity(
                RegisterActivity.getIntent(this)
            )
        }
        binding.tittleForgetPassword.setOnClickListener {
            startActivity(ForgetPasswordActivity.getIntent(this))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    fun observerUser() {
        viewModel.user.observe(this, Observer {
            SavePrefs(this, User::class.java).save(it)
            MyApp.getApp().appPreferencesHelper.setLogin(true)
            startActivity(
                MainActivity.getIntent(this)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finishAffinity()
        })

        viewModel.needRegister.observe(this, Observer {
            val intent:Intent = RegisterActivity.getIntent(this)
            intent.putExtra(SOCIAL_RES,socialRes)
            startActivity(intent)
        })
    }


}