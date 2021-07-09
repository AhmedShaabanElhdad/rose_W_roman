package com.smartzone.myapp.ui.change_password

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.reponse.Setting
import com.smartzone.myapp.data.repositery.AccountRepositery
import com.smartzone.myapp.data.repositery.GeneralRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class ChangePasswordViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val generalRepositery: GeneralRepositery,
    private val authenRepositery: AccountRepositery
) : BaseViewModel() {

    val setting=MutableLiveData<Setting>()
    val success=MutableLiveData<Boolean>()
    fun getSetting(){
        setLoading(true)
        add {
            generalRepositery.getSetting()
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        setting.value=it.Setting
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

    fun sendFeadBack(name:String,mobile:String,message:String){
        setLoading(true)
        add {
            generalRepositery.sendFeadBack(mobile,name,message)
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

    fun changePassword(password:String){
        setLoading(true)
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        add {
            authenRepositery.changePassword(password,user!!.id)
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

}