package com.smartzone.myapp.utilis

import android.util.Log
import com.smartzone.diva_wear.BuildConfig

class LogUtil {

    companion object {
        @JvmStatic
        fun info(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                // do something for a debug build
                Log.i(tag, message)
            }
        }

        @JvmStatic
        fun error(tag: String, message: String?) {
            if (BuildConfig.DEBUG) {
                // do something for a debug build
                if (message != null)
                    Log.e(tag, message)
            }
        }

        @JvmStatic
        fun debug(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                // do something for a debug build
                Log.d(tag, message)
            }
        }
    }

}
