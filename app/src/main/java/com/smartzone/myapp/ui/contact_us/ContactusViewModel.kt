package com.smartzone.myapp.ui.contact_us

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.data.reponse.Setting
import com.smartzone.myapp.data.repositery.GeneralRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class ContactusViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val generalRepositery: GeneralRepositery
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

}