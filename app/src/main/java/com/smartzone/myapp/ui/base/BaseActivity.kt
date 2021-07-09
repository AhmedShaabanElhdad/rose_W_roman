package com.smartzone.myapp.ui.base



import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.ErrorMessageType
import com.smartzone.myapp.ui.notification.NotificationActivity
import com.smartzone.myapp.utilis.AppUtils
import com.smartzone.myapp.ui.dailogs.LoadingDialog
import com.smartzone.myapp.utilis.LocaleUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private lateinit var mViewDataBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //status bar account
        // makeStatusBarTransparent()
        if (BuildConfig.FLAVOR == "horizon")
            LocaleUtils.updateConfig(context = baseContext)
        //LocaleUtils.updateConfig(context = baseContext)

        getHashing()
        performDataBinding()
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    private var mProgressDialog: LoadingDialog? = null


    override fun onStart() {
        super.onStart()
        //LocaleUtils.updateConfig(this)
    }

    override fun onPause() {
        super.onPause()

    }


    fun getHashing(){
        // Add code to print out the key hash

        // Add code to print out the key hash
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", android.util.Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }


    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun openActivityOnTokenExpire() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showSnackbar(
        view: View,
        message: String,
        type: ErrorMessageType?,
        duration: Int = Snackbar.LENGTH_SHORT,
        callback: () -> Unit
    ) {
        val snackbar = Snackbar
            .make(view, message, duration)
        type?.let {
            when (type) {
                ErrorMessageType.SUCCESS -> snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.successColor
                    )
                )
                ErrorMessageType.ERROR -> snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.errColor
                    )
                )
                ErrorMessageType.WARNING -> snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.yellowWarning
                    )
                )
            }
        }
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                callback()
            }
        })
        snackbar.show()
    }

    fun showSnackbar(
        message: String,
        type: ErrorMessageType?,
        duration: Int = Snackbar.LENGTH_SHORT
    ) {
        val view = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar
            .make(findViewById(android.R.id.content), message, duration)
        type?.let {
            when (it) {
                ErrorMessageType.SUCCESS -> snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.successColor
                    )
                )
                ErrorMessageType.ERROR -> snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.errColor
                    )
                )
                ErrorMessageType.WARNING -> snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.yellowWarning
                    )
                )
            }
        }
        snackbar.show()
    }

    open fun showLoading() {
        hideLoading()
        mProgressDialog = AppUtils.showLoadingDialog(this)
        mProgressDialog?.showDialog()
    }

    open fun hideLoading() {
        mProgressDialog?.dism()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewDataBinding.executePendingBindings()

        performObserveLoading()

        performErrorMessage()
        performObserveErrorNetwork()
    }

    fun performErrorMessage() {
        getViewModel()?.let {
            it.message.observe(this, Observer { message ->
                showDialogInfo(message)
            })
        }
    }

    fun showDialogInfo(message: String) {
        showSnackbar(message, ErrorMessageType.ERROR,10000)
    }

    fun performObserveErrorNetwork() {
        getViewModel()?.let {
            it.errorNetwork.observe(this, Observer { throwable ->
                showDialogInfo(throwable.message!!)
            })
        }
    }

    fun performObserveLoading() {
        getViewModel()?.let {
            it.isLoading.observe(this, Observer { obser ->
                if (obser) {
                    showLoading()
                } else
                    hideLoading()
            })
        }
    }

    abstract fun getViewModel(): BaseViewModel?


    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

    override fun attachBaseContext(newBase: Context) {
        var newBase = newBase
        val lang: String ="ar"
//            EduraApp.instance.appPreferencesHelper.get(
//                LANG_KEY, "en"
//            )!!
        val locale = Locale(lang)
        val config =
            Configuration(newBase.resources.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        newBase = newBase.createConfigurationContext(config)
        super.attachBaseContext(newBase)
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(baseContext.resources.configuration)
    }

    override fun onDestroy() {
        super.onDestroy()
        getViewModel()?.onClear()
    }

    fun openNotification(){
        startActivity(NotificationActivity.getIntent(this))
    }


}