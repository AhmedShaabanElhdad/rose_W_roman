package com.smartzone.myapp.ui.base
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.ErrorMessageType

abstract class BaseDialog:DialogFragment() {
    private var mActivity: BaseActivity<*>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            this.mActivity = context
        }
    }

    fun showSnackbar(
        message: String,
        type: ErrorMessageType?,
        duration: Int = Snackbar.LENGTH_SHORT
    ){
        mActivity?.showSnackbar(message,type,duration)
    }
    abstract fun getViewModel(): BaseViewModel?


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // creating the fullscreen dialog
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    fun dismissDialog(tag: String) {
        dismiss()
    }
    fun getBaseActivity(): BaseActivity<*> {
        return mActivity!!
    }

    fun hideKeyboard() {
        if (mActivity != null) {
            mActivity?.hideKeyboard()
        }
    }


    open fun showloading() {
        if (mActivity != null) {
            mActivity?.showLoading()
        }
    }
    open fun hidLoading() {
        if (mActivity != null) {
            mActivity?.hideLoading()
        }
    }

    override fun onResume() {
        val window = dialog!!.window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window.attributes.windowAnimations = R.style.PopupAnimation
        window.setGravity(Gravity.BOTTOM)
        super.onResume()
    }



    fun performObserveErrorNetwork() {
        getViewModel()?.let {
            it.errorNetwork.observe(this, Observer {
                showSnackbar(it.message!!, ErrorMessageType.ERROR, 1000)
            })
        }
    }
     open fun performObserveLoading() {
        getViewModel()?.let {
            it.isLoading.observe(this, Observer {obser->
                if (obser) {
                    showloading()
                } else
                    hidLoading()
            })
        }
    }
}