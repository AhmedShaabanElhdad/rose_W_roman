package com.smartzone.myapp.data.repositery

import com.smartzone.myapp.data.APIHelper
import com.smartzone.myapp.data.reponse.RequestResponse
import io.reactivex.Single

class RequestRepositery(private val apiHelper: APIHelper){
    fun getcart(user_id:String,request:String?,id:String?):Single<RequestResponse>{
        return apiHelper.getRequests(user_id,request,id)
    }
}