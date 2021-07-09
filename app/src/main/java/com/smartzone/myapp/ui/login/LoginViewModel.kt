package com.smartzone.myapp.ui.login

import androidx.lifecycle.MutableLiveData
import com.google.firebase.iid.FirebaseInstanceId
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.repositery.AccountRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class LoginViewModel(
    private val accountRepositery: AccountRepositery,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val user=MutableLiveData<User>()
    val needRegister=MutableLiveData<Boolean>()

    fun login(userName:String,password:String){
        setLoading(true)
        val token = FirebaseInstanceId.getInstance().token
        add {
            accountRepositery.login(userName,password,token)
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        user.value=it.user
                    }else{
                        setErrorMessage(it.message)
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

    fun checkInput(userName:String){
        setLoading(true)
        add {
            accountRepositery.checklogin(userName)
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        user.value=it.user
                    }else{
                        needRegister.value=true
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }
}