package com.smartzone.myapp.data.repositery

import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.APIHelper
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.reponse.MainResonse
import com.smartzone.myapp.data.reponse.ResponseSliders
import com.smartzone.myapp.data.utils.Result
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.data.reponse.ResponseCategory
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single

class HomeRepository(private val apiHelper: APIHelper) {

    fun getMain(): Observable<Result<MainResonse>> {
        val user = SavePrefs(MyApp.getApp(), User::class.java).load()

        return apiHelper.getMain(user?.id)
            .flatMap(io.reactivex.functions.Function<MainResonse, ObservableSource<out Result<MainResonse>>> { response ->
                if (response.status) {
                    return@Function Observable.just(Result.Success(response))
                } else {
                    return@Function Observable.just(Result.Failure("No Result"))
                }
            })
    }
    fun getLink(): Observable<Result<MainResonse>> {
        return apiHelper.getLinks()
            .flatMap(io.reactivex.functions.Function<MainResonse, ObservableSource<out Result<MainResonse>>> { response ->
                if (response.status) {
                    return@Function Observable.just(Result.Success(response))
                } else {
                    return@Function Observable.just(Result.Failure("No Result"))
                }
            })
    }

    fun getCategories(): Observable<Result<ResponseCategory>> {
        return apiHelper.getCategories()
            .flatMap(io.reactivex.functions.Function<ResponseCategory, ObservableSource<out Result<ResponseCategory>>> { response ->
                if (response.status) {
                    return@Function Observable.just(Result.Success(response))
                } else {
                    return@Function Observable.just(Result.Failure("No Result"))
                }
            })
    }
    fun getSubCategories(mainId:String): Observable<Result<ResponseCategory>> {

        return apiHelper.getSubCategories(mainId)
            .flatMap(io.reactivex.functions.Function<ResponseCategory, ObservableSource<out Result<ResponseCategory>>> { response ->
                if (response.status) {
                    return@Function Observable.just(Result.Success(response))
                } else {
                    return@Function Observable.just(Result.Failure("No Result"))
                }
            })
    }

    fun getSlider(): Single<ResponseSliders> {
        return apiHelper.getSlider()
    }


}