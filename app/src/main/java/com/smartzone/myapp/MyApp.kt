package com.smartzone.myapp

import android.app.Application
import android.util.Log
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.myapp.data.AppPreferencesHelper
import com.smartzone.myapp.di.AppInjector
import com.smartzone.myapp.utilis.CartManger
import com.smartzone.myapp.utilis.LocaleUtils

import org.koin.android.ext.android.get


class MyApp : Application() {

    lateinit var appPreferencesHelper: AppPreferencesHelper

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.FLAVOR == "horizon")
            LocaleUtils.updateConfig(context = baseContext)

        AppInjector.init(this)
        instance = this
        appPreferencesHelper = get()

        Log.d(
            "Application",
            "The host is " + BuildConfig.URL_BASE + " for flavor " + BuildConfig.FLAVOR
        );

    }


    fun getappPreferencesHelper(): AppPreferencesHelper {
        return appPreferencesHelper
    }

    companion object {
        lateinit var instance: MyApp
        var cartManager: CartManger? = null
        fun getApp(): MyApp {
            return instance
        }

        fun getCart(): CartManger {
            if (cartManager == null) {
                cartManager = CartManger()
            }
            return cartManager!!
        }
    }


}