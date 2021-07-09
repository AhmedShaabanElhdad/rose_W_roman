package com.smartzone.myapp.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.myapp.ui.login.LoginActivity
import com.smartzone.myapp.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val token = FirebaseInstanceId.getInstance().token

        //LocaleUtils.updateConfig(context = baseContext)
        if (token!=null){
            Log.e("token",token)
        }else{
            Log.e("token","nulllllllllllll")
        }
        Handler(mainLooper).postDelayed({
            if (MyApp.getApp().appPreferencesHelper.isLogin()){
                startActivity(MainActivity.getIntent(this))
                finish()
            }else{
                startActivity(LoginActivity.getIntent(this))
                finish()
            }
        },1000)
    }
}