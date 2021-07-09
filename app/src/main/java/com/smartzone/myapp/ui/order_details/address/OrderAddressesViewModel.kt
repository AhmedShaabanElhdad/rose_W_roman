package com.smartzone.myapp.ui.order_details.address

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.Address
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.repositery.AccountRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class OrderAddressesViewModel(
    private val accountRepositery: AccountRepositery,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    val addresses=MutableLiveData<List<Address>>()
    val success=MutableLiveData<Boolean>(false)


    fun getAddresses(){
        setLoading(true)
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        add {
            accountRepositery.getAddresses(user?.id ?: "0")
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        addresses.value=it.list
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }


    fun saveAddress(name: String, longitude: Double?, latitude: Double?, address: String?){
        setLoading(true)
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()
        add {
            accountRepositery.saveAddresses(name,user?.id ?: "0",longitude,latitude,address)
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        success.value = true
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }
}