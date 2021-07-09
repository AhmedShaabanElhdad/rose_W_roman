package com.smartzone.myapp.ui.order_details.details

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.data.pojo.City
import com.smartzone.myapp.data.repositery.GeneralRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class OrderDetailsViewModel(
    private val generalRepositery: GeneralRepositery,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    val cities=MutableLiveData<ArrayList<City>>()

    fun getCity(showingLoader:Boolean = true){
        setLoading(showingLoader)
        add {
            generalRepositery.getCity()
                .with(schedulerProvider).subscribe({
                    if (it.status){
                        cities.value=it.cities as ArrayList<City>
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }
}