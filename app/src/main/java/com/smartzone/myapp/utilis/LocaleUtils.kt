/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.smartzone.myapp.utilis

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.ui.main.MainActivity
import java.util.*


class LocaleUtils {

    companion object {
        private var sLocale: Locale? = null
        private var pref: SavePrefs<User>? = null


        fun changeLanguage(context: Context) {
            pref = SavePrefs(context, User::class.java)

            pref!!.changeLanguage()

            updateConfig(context)



            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            (context as AppCompatActivity).finish()
        }

        fun updateConfig(context: Context) {
            if (pref == null)
                pref = SavePrefs(context, User::class.java)

            if (pref!!.getLanguage().equals(LanguageType.ARABIC.lang)) {
                    setLocale(Locale("ar"))

            } else if (pref!!.getLanguage().equals(LanguageType.ENGLISH.lang)) {
                    setLocale(Locale("en"))
            } else {
                    setLocale(Locale("en"))
            }

            val resources = context.applicationContext.resources

            val overrideConfiguration = resources.configuration

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                overrideConfiguration.setLocale(sLocale)
            } else {
                overrideConfiguration.locale = sLocale
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.updateConfiguration(
                    overrideConfiguration,
                    context.resources.displayMetrics
                )
            } else {
                resources.updateConfiguration(overrideConfiguration, null)
            }
        }


        private fun setLocale(locale: Locale) {
            sLocale = locale
            if (sLocale != null) {
                Locale.setDefault(sLocale)
            }
        }

    }
}