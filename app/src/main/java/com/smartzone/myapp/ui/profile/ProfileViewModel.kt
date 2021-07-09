package com.smartzone.myapp.ui.profile

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.data.pojo.City
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.repositery.AccountRepositery
import com.smartzone.myapp.data.repositery.GeneralRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with
import java.io.File

class ProfileViewModel(
    private val accountRepositery: AccountRepositery,
    private val schedulerProvider: SchedulerProvider,
    private val generalRepositery: GeneralRepositery
) : BaseViewModel() {

    val user=MutableLiveData<User>()
    val cities=MutableLiveData<List<City>>()
    var pathImage:String?=null
    fun editProfile(  mobile: String,
                name:String,
                city_id:String,
                id:String
    ){
        setLoading(true)
        add {
            accountRepositery.editProfile(mobile,name,city_id,id,pathImage)
                .with(schedulerProvider).subscribe({
                    if (it.status){

                        user.value=it.user
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

    fun getCity(){
        setLoading(true)
        add {
            generalRepositery.getCity()
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        cities.value=it.cities
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

    fun uploadImage(file:File){
        setLoading(true)
        val list=ArrayList<File>()
        list.add(file)
        add {
            generalRepositery.uploadImage(list)
                .with(schedulerProvider).subscribe({
                    if (it.images.isNotEmpty()){
                        pathImage=it.images[0]
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

}