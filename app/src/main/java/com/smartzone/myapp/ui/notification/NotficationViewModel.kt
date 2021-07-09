package com.smartzone.myapp.ui.notification

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.Notification
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with
import com.smartzone.myapp.data.repositery.NotifcationRepositery

class NotficationViewModel(
    private val notifcationRepositery: NotifcationRepositery,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val notfications=MutableLiveData<List<Notification>>()
    fun getNotifcations(
    ){
        setLoading(true)
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        add {
            notifcationRepositery.getNotification(user_id = user?.id)
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        notfications.value=it.notifications
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

}