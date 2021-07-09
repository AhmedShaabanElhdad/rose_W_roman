package com.smartzone.horizon.ui.main.home

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.data.pojo.Slider
import com.smartzone.myapp.data.reponse.MainResonse
import com.smartzone.myapp.data.reponse.ResponseCategory
import com.smartzone.myapp.data.repositery.HomeRepository
import com.smartzone.myapp.data.utils.Result
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class HomeViewModel2(private val homeRepository: HomeRepository, private val schedulerProvider: SchedulerProvider) : BaseViewModel() {
    val mainResponse=MutableLiveData<MainResonse>()
    val linkResponse=MutableLiveData<MainResonse>()
    val slider=MutableLiveData<List<Slider>>()
    val categoriesResponse=MutableLiveData<ResponseCategory>()


    fun loadData(){
        setLoading(true)
        add {

            homeRepository.getMain().with(schedulerProvider)
                .subscribe({
                    if (it is Result.Success){
                        mainResponse.value=it.data
                    }else if (it is Result.Failure){
                        setErrorMessage(it.exception)
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }
    fun loadLink(){
        setLoading(true)
        add {

            homeRepository.getLink().with(schedulerProvider)
                .subscribe({
                    if (it is Result.Success){
                        linkResponse.value=it.data
                    }else if (it is Result.Failure){
                        setErrorMessage(it.exception)
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }

    fun getSlider(){
        add {
            homeRepository.getSlider().with(schedulerProvider)
                .subscribe({
                    if (it.status){
                        slider.value=it.sliders
                    }else{
                        slider.value= ArrayList()
                    }
                },{
                    setErrorNetwork(it)
                })
        }
    }

    fun getCategories(){
        setLoading(true)
        add {
            homeRepository.getCategories().with(schedulerProvider)
                .subscribe({
                    if (it is Result.Success){
                        categoriesResponse.value=it.data

                    }else if (it is Result.Failure){
                        setErrorMessage(it.exception)
                    }
                    setLoading(false)
                },{
                    setErrorNetwork(it)
                })
        }
    }
}