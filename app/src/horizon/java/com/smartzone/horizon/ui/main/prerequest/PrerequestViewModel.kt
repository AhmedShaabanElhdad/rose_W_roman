package com.smartzone.horizon.ui.main.prerequest

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.reponse.Setting
import com.smartzone.myapp.data.repositery.GeneralRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with
import java.io.File

class PrerequestViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val generalRepositery: GeneralRepositery
) : BaseViewModel() {

    val success=MutableLiveData<Boolean>()

    fun sendPrescription(name:String, mobile:String, message:String){
        setLoading(true)
        val user = SavePrefs(MyApp.getApp(), User::class.java).load()
        add {
            generalRepositery.sendPrescription(userId = user!!.id,title = name,body = message,image = pathImage.value)
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

    var pathImage=MutableLiveData<String>("")

    fun uploadImage(file: File){
        setLoading(true)
        val list=ArrayList<File>()
        list.add(file)
        add {
            generalRepositery.uploadImage(list)
                .with(schedulerProvider).subscribe({
                    if (it.images.isNotEmpty()){
                        pathImage.value= it.images[0]
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

}