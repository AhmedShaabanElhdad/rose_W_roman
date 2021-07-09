package com.smartzone.diva_wear.data.repositery

import com.smartzone.myapp.data.APIHelper
import com.smartzone.myapp.data.reponse.ResponseFavourite
import com.smartzone.myapp.data.reponse.ResponseProducts
import io.reactivex.Single

class FavouriteRepositery(private val apiHelper: APIHelper){
    fun add_favourite(user_id:String,id:String):Single<ResponseFavourite>{
        return apiHelper.addFavourite(user_id,id = id)
    }

    fun get_favourite(user_id:String,search:String?=null):Single<ResponseProducts>{
        return apiHelper.getFavourite(user_id = user_id,search = search)
    }
}