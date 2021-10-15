package com.smartzone.myapp.utilis

import android.content.Context
import android.content.SharedPreferences

import com.google.gson.Gson
import com.smartzone.diva_wear.BuildConfig


enum class LanguageType(val lang: String) {
    ARABIC("ar"),
    ENGLISH("en")
}

class SavePrefs<T>(activity: Context, cls: Class<T>) {

    private val USER_PREFS_FILE_NAME: String
    private val DATA = "DATA"
    private val LANG = "lang"
    private var prefs: SharedPreferences? = null
    private val cls: Class<*>

    init {
        this.cls = cls
        USER_PREFS_FILE_NAME = cls.name
        prefs = activity.getSharedPreferences(
            USER_PREFS_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun save(obj: T) {
        val editor = prefs!!.edit()
        val data = Gson().toJson(obj)
        editor.putString(DATA, data)
        editor.apply()
    }

    fun load(): T? {
        val data = prefs!!.getString(DATA, "")
        var objectConvert: T?
        try {
            objectConvert = (Gson().fromJson<T>(data, cls) as T)
        } catch (ex: Exception) {
            return null
        }
        return objectConvert
    }


    fun changeLanguage() {
        val editor = prefs!!.edit()
        if (prefs!!.getString(LANG, LanguageType.ARABIC.lang).equals(LanguageType.ARABIC.lang))
            editor.putString(LANG, LanguageType.ENGLISH.lang)
        else
            editor.putString(LANG, LanguageType.ARABIC.lang)
        editor.apply()
    }

    fun getLanguage(): String {
        return if (BuildConfig.FLAVOR == "horizon")
            prefs!!.getString(LANG, LanguageType.ARABIC.lang)!!
        else
            prefs!!.getString(LANG, LanguageType.ENGLISH.lang)!!
    }


    fun clear() {
        prefs!!.edit().clear().apply()
    }

}
