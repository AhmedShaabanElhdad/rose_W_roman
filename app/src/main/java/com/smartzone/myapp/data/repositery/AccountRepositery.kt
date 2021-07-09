package com.smartzone.myapp.data.repositery

import com.smartzone.myapp.data.APIHelper
import com.smartzone.myapp.data.reponse.AddressResponse
import com.smartzone.myapp.data.reponse.BaseObjectResponse
import com.smartzone.myapp.data.reponse.ResponseSendFedback
import com.smartzone.myapp.data.reponse.ResponseUser
import io.reactivex.Single

class AccountRepositery(private val apiHelper: APIHelper) {
    fun login(userName: String, password: String, token: String?): Single<ResponseUser> {
        return apiHelper.login(userName, password, token)
    }

    fun checklogin(userName: String, mobile: String = ""): Single<ResponseUser> {
        return apiHelper.checkInput(username = userName)
    }

    fun validateUserInput(userName: String, mobile: String = ""): Single<ResponseUser> {
        return apiHelper.validateUserInput(username = userName, mobile = mobile)
    }

    fun register(
        mobile: String,
        password: String,
        token: String?,
        name: String,
        city_id: String?,
        email: String?

    ): Single<ResponseUser> {
        return apiHelper.signUp(mobile, password, token, name, city_id, email)
    }


    fun editProfile(
        mobile: String,
        name: String,
        city_id: String?,
        id: String,
        image: String? = null
    ): Single<ResponseUser> {
        return apiHelper.edit(mobile, name, city_id, id, image)
    }


    fun forgetPassword(mobile: String): Single<BaseObjectResponse> {
        return apiHelper.forgetPassword(mobile)
    }


    fun getAddresses(user_id: String): Single<AddressResponse> {
        return apiHelper.getAddresses(user_id)
    }

    fun saveAddresses(
        name: String,
        user_id: String,
        longitude: Double?,
        latitude: Double?,
        address: String?
    ): Single<BaseObjectResponse> {
        return apiHelper.saveAddress(name, user_id, longitude, latitude, address)
    }

    fun changePassword(password: String, id: String): Single<ResponseSendFedback> {
        return apiHelper.changePassword(password,id)
    }

}