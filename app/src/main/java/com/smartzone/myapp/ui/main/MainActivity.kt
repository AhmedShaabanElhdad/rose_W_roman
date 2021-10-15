package com.smartzone.myapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityMainBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.dailogs.PleaseRegisterDialog


class
MainActivity : BaseActivity<ActivityMainBinding>() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.navigation_home,
//            R.id.navigation_dashboard,
//            R.id.navigation_notifications
//        ))
        //setupActionBarWithNavController(navController, appBarConfiguration)
        val cart = intent.extras?.getBoolean("cart")
        cart?.let {
            navController.navigate(R.id.cart)
        }
        if (MyApp.getApp().appPreferencesHelper.isLogin()) {
            binding.navView.setupWithNavController(navController)
        } else {
            binding.navView.setOnNavigationItemSelectedListener(navListener);
        }
        binding.addActivity.setOnClickListener {
            navController.navigate(R.id.home)
        }
        //navController.
    }

    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.getItemId()) {
                R.id.navigation_cart,R.id.navigation_orders,R.id.navigation_offers,R.id.navigation_setting ->  PleaseRegisterDialog(this@MainActivity).show()
//                R.id.navigation_cart,R.id.navigation_orders,R.id.navigation_categories,R.id.navigation_setting ->  PleaseRegisterDialog(this@MainActivity).show()
            }
            true
        }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }


    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}