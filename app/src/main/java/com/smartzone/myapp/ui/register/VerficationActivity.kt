package com.smartzone.myapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityVerficationBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.utilis.SavePrefs
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class VerficationActivity : BaseActivity<ActivityVerficationBinding>() {
    val viewModel: RegisterViewModel by viewModel()
    lateinit var binding: ActivityVerficationBinding
    val TAG = "VerficationActivity"

    lateinit var callbacks: OnVerificationStateChangedCallbacks

    var mResendToken: ForceResendingToken? = null
    var mVerificationId: String? = null
    var data: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        data = intent.extras?.getSerializable("data") as User
        observerUser()
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token

                // ...
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$p0")
                signInWithPhoneAuthCredential(p0)
                Toast.makeText(this@VerficationActivity, "complete", Toast.LENGTH_LONG).show()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", p0)
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@VerficationActivity, "error phone", Toast.LENGTH_LONG)
                        .show()
                } else if (p0 is FirebaseTooManyRequestsException) {
                    Toast.makeText(this@VerficationActivity, "BlockedPhone", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this@VerficationActivity, "error", Toast.LENGTH_LONG).show()
                }
            }
        }
        FirebaseAuth.getInstance().setLanguageCode("ar")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+2${data!!.phone}", // Phone number to verify
            30, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks

        binding.btnSignUp.setOnClickListener {
            if (!binding.firstPinView.text.isNullOrBlank()) {
                val credential =
                    PhoneAuthProvider.getCredential(
                        mVerificationId!!,
                        binding.firstPinView.text.toString()
                    )
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "success")
                    viewModel.signUP(
                        password = data!!.password,
                        city_id = data!!.idCity,
                        name = data!!.name,
                        mobile = data!!.phone,
                        email = data!!.username,
                        token = FirebaseInstanceId.getInstance().token
                    )
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun observerUser() {
        viewModel.user.observe(this, Observer {
            SavePrefs(this, com.smartzone.myapp.data.pojo.User::class.java).save(it)
            MyApp.getApp().appPreferencesHelper.setLogin(true)
            startActivity(
                MainActivity.getIntent(this)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finishAffinity()
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_verfication
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        Firebase.auth.signOut()
    }


    companion object {
        fun getIntent(context: Context): Intent = Intent(context, VerficationActivity::class.java)
    }
}