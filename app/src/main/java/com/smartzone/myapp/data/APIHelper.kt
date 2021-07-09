package com.smartzone.myapp.data

import com.smartzone.myapp.data.reponse.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface APIHelper {

    @GET("main/get_category")
    fun getCategories(): Observable<ResponseCategory>

    @GET("main/get_category")
    fun getSubCategories(
        @Query("main_id") main_id: String?
    ): Observable<ResponseCategory>


    @GET("main/get_links")
    fun getLinks(
    ): Observable<MainResonse>

    @GET("main/get_main")
    fun getMain(
        @Query("user_id") user_id: String? = "0"
    ): Observable<MainResonse>

    @GET("main/get_home_sliders")
    fun getSlider(): Single<ResponseSliders>

    @GET("main/get_products")
    fun getProducts(
        @Query("category_id") category_id: String? = null,
        @Query("search") search: String? = null,
        @Query("id") id: String? = null,
        @Query("product_id") product_id: String? = null,
        @Query("user_id") user_id: String? = null,
        @Query("sort") sort: String? = "low"
    ): Single<ResponseProducts>


    @GET("main/get_products")
    fun search(
        @Query("search") search: String? = null,
        @Query("user_id") user_id: String? = null,
        @Query("sort") sort: String? = "low"
    ): Single<ResponseProducts>


    @GET("main/get_products")
    fun getOffers(
        @Query("deal") deal: String = "1"
    ): Single<ResponseProducts>


    @GET("main/get_products")
    fun getMainProducts(
        @Query("maincategory") category_id: String? = null,
        @Query("sort") sort: String? = "low",
        @Query("user_id") user_id: String? = null
    ): Single<ResponseProducts>

    @GET("global/get_favourite")
    fun getFavourite(
        @Query("search") search: String? = null,
        @Query("user_id") user_id: String
    ): Single<ResponseProducts>

    @GET("global/add_favourite")
    fun addFavourite(
        @Query("user_id") user_id: String,
        @Query("id") id: String
    ): Single<ResponseFavourite>

    @GET("global/login")
    fun login(
        @Query("mobile") mobile: String,
        @Query("password") password: String,
        @Query("token") token: String?
    ): Single<ResponseUser>

    @GET("global/check_input")
    fun checkInput(
        @Query("social") social: Boolean = true,
        @Query("username") username: String
    ): Single<ResponseUser>

    @GET("global/check_input")
    fun validateUserInput(
        @Query("username") username: String,
        @Query("mobile") mobile: String
    ): Single<ResponseUser>

    @GET("global/cities")
    fun getCity(): Single<ResponseCity>

    @GET("global/signup")
    fun signUp(
        @Query("mobile") mobile: String,
        @Query("password") password: String,
        @Query("token") token: String?,
        @Query("name") name: String,
        @Query("city_id") city_id: String?,
        @Query("username") email: String?
    ): Single<ResponseUser>

    @GET("global/edit")
    fun edit(
        @Query("mobile") mobile: String,
        @Query("name") name: String,
        @Query("city_id") city_id: String?,
        @Query("id") id: String,
        @Query("image") image: String?
    ): Single<ResponseUser>

    @GET("global/forget_password")
    fun forgetPassword(
        @Query("mobile") mobile: String
    ): Single<BaseObjectResponse>


    @GET("main/check_promocode")
    fun check_promocode(
        @Query("total_price") total_price: String,
        @Query("promocode") promocode: String,
        @Query("user_id") user_id: String
    ): Single<PromoCodeResponse>

    @GET("main/add_cart_multiple")
    fun add_cart_multiple(
        @Query("user_id") user_id: String,
        @Query("city_id") city_id: String,
        @QueryMap meta: Map<String, String>,
        @Query("promocode") promocode: String = "0"
    ): Single<BaseObjectResponse>

    @GET("main/get_cart")
    fun getRequests(
        @Query("user_id") user_id: String?,
        @Query("request") request: String?,
        @Query("id") id: String?
    ): Single<RequestResponse>

    @GET("main/get_notifications")
    fun getNotifications(
        @Query("user_id") user_id: String?
    ): Single<ResponseNotification>

    @POST("global/upload_image_api")
    @Multipart
    fun uploadFiles(
        @Part parts: List<MultipartBody.Part>
    ): Single<ResponseUploadImage>

    @GET("global/settings")
    fun getSettings(
    ): Single<ResponseSetting>

    @GET("global/send_feedback")
    fun sendFeedBack(
        @Query("mobile") mobile: String,
        @Query("name") name: String,
        @Query("message") message: String?
    ): Single<ResponseSendFedback>


    @GET("global/send_prescription")
    fun sendPrescription(
        @Query("user_id") userId: String,
        @Query("title") title: String,
        @Query("body") body: String?,
        @Query("image") image: String?
    ): Single<ResponseSendFedback>


    @GET("global/save_address")
    fun saveAddress(
        @Query("name") name: String,
        @Query("user_id") user_id: String,
        @Query("longitude") longitude: Double?,
        @Query("latitude") latitude: Double?,
        @Query("address") address: String?
    ): Single<BaseObjectResponse>

    @GET("global/get_address")
    fun getAddresses(
        @Query("user_id") user_id: String
    ): Single<AddressResponse>

    @GET("global/get_address")
    fun changePassword(
        @Query("new_password") password: String,
        @Query("id") user_id: String
    ): Single<ResponseSendFedback>

}