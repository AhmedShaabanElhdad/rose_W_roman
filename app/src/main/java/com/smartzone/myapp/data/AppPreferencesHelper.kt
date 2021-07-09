package com.smartzone.myapp.data

import android.content.Context
import android.content.SharedPreferences
import com.smartzone.myapp.utilis.prefFileName

class AppPreferencesHelper(val context: Context) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    fun getLang():String?{
        return prefs.getString("lang","ar")
    }
    fun setLang(lang:String){
        val editor = prefs.edit()
        editor.putString("lang",lang).apply()
    }

    fun isLogin():Boolean{
        return prefs.getBoolean("isLogin",false)
    }
    fun setLogin(value: Boolean){
        val editor = prefs.edit()
        editor.putBoolean("isLogin",value).apply()
    }
}