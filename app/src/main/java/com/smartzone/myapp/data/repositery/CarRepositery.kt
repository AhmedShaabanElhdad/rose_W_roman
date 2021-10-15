package com.smartzone.myapp.data.repositery

import com.smartzone.myapp.data.APIHelper
import com.smartzone.myapp.data.reponse.AddressResponse
import com.smartzone.myapp.data.reponse.BaseObjectResponse
import com.smartzone.myapp.data.reponse.PromoCodeResponse
import io.reactivex.Single

class CarRepositery(private val apiHelper: APIHelper){

    fun add_cart(user_id:String,city_id:String,promocode:String,map:Map<String,String>,lat:String,lon:String,address:String):Single<BaseObjectResponse>{
        return apiHelper.add_cart_multiple(user_id,city_id,map,promocode,lat,lon,address)
    }

    fun checkPromoCode(total_price:String,promocode:String,user_id:String):Single<PromoCodeResponse>{
        return apiHelper.check_promocode(total_price,promocode,user_id)
    }
}