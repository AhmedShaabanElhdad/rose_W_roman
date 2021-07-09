package com.smartzone.myapp.data.repositery

import com.smartzone.myapp.data.APIHelper
import com.smartzone.myapp.data.reponse.ResponseNotification
import io.reactivex.Single

class NotifcationRepositery(private val apiHelper: APIHelper){
    fun getNotification(user_id:String?):Single<ResponseNotification>{
        return apiHelper.getNotifications(user_id)
    }
}