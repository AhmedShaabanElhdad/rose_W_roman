package com.smartzone.myapp.data.repositery

import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.APIHelper
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.reponse.ResponseProducts
import com.smartzone.myapp.utilis.SavePrefs
import io.reactivex.Single

class ProductRepositery(private val apiHelper: APIHelper){

    fun getProducts(category_id:String?=null,search:String?=null,id:String?=null,sort:String?=null):Single<ResponseProducts>{
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        return apiHelper.getProducts(category_id = category_id,user_id = user?.id,search = search,id = id
        ,sort = sort
        )
    }
    fun search(search:String?=null,sort:String?=null):Single<ResponseProducts>{
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        return apiHelper.search(user_id = user?.id,search = search,sort = sort
        )
    }
    fun getOffers():Single<ResponseProducts>{
        return apiHelper.getOffers()
    }

    fun getProductSimilar(category_id:String?=null,product_id:String?=null,sort:String?=null):Single<ResponseProducts>{
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        return apiHelper.getProducts(category_id = category_id,
            product_id = product_id,user_id = user?.id
            ,sort = sort
        )
    }

    fun getMainProduct(category_id:String?=null,sort:String?=null):Single<ResponseProducts>{
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        return apiHelper.getMainProducts(category_id = category_id,sort = sort,user_id = user?.id
        )
    }
}