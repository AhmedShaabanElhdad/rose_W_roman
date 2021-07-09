package com.smartzone.myapp.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.myapp.data.ErrorMessageType
import com.smartzone.myapp.utilis.LocaleUtils


abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    private var mActivity: BaseActivity<*>? = null
  //  private lateinit var mRootView: View
    private lateinit var mViewDataBinding: T

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): BaseViewModel?
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            this.mActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (BuildConfig.FLAVOR == "horizon")
            LocaleUtils.updateConfig(context = requireActivity().applicationContext)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        return mViewDataBinding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = this
      //  mViewDataBinding.executePendingBindings()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        performObserveLoading()
        performErrorMessage()
        performErrorNetworkMessage()
    }

    fun performErrorNetworkMessage(){
        getViewModel()?.errorNetwork?.observe(viewLifecycleOwner, Observer {
            showDialogInfo(it.message!!)
        })
    }


    fun hideKeyboard() {
        mActivity?.hideKeyboard()
    }

    fun openActivityOnTokenExpire() {
        mActivity?.openActivityOnTokenExpire()
    }

    fun showSnackbar(
        message: String,
        type: ErrorMessageType?,
        duration: Int = Snackbar.LENGTH_SHORT
    ) {
        mActivity?.showSnackbar(message, type, duration)
    }

    fun showSnackbar(
        view: View,
        message: String,
        type: ErrorMessageType?,
        duration: Int = Snackbar.LENGTH_SHORT,
        callback: () -> Unit
    ) {
        mActivity?.showSnackbar(view, message, type, duration, callback)
    }

    fun showToast(message: String) {
        mActivity?.showToast(message)
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        getViewModel()?.onClear()
    }
    fun getViewDataBinding(): T {
        return mViewDataBinding
    }


    open fun performErrorMessage() {
        getViewModel()?.message?.observe(viewLifecycleOwner, Observer { message ->
            showDialogInfo(message)
        })
    }
    fun showDialogInfo(message: String) {
        activity?.let {
            showSnackbar(message, ErrorMessageType.ERROR,10000)
        }
    }

    open fun performObserveLoading() {
        getViewModel()?.isLoading?.observe(viewLifecycleOwner, Observer {
            if (it) {
                showloading()
            } else
                hidLoading()
        })
    }

}