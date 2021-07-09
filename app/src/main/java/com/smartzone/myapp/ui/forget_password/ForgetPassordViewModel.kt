package com.smartzone.myapp.ui.forget_password

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.data.repositery.AccountRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class ForgetPassordViewModel(
    private val accountRepositery: AccountRepositery,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val success=MutableLiveData<Boolean>()
    fun forgetPassword(  mobile: String
    ){
        setLoading(true)
        add {
            accountRepositery.forgetPassword(mobile)
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        success.value=true
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

    fun onClickConfirm(){

    }
}