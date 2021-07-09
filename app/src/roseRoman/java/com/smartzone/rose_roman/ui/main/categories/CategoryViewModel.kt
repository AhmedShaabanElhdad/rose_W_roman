package com.smartzone.rose_roman.ui.main.categories

import androidx.lifecycle.MutableLiveData
import com.smartzone.rose_roman.data.repositery.HomeRepository
import com.smartzone.rose_roman.data.utils.Result
import com.smartzone.rose_roman.ui.base.BaseViewModel
import com.smartzone.rose_roman.utilis.rx.SchedulerProvider
import com.smartzone.rose_roman.utilis.rx.with
import com.smartzone.rose_roman.data.pojo.Slider
import com.smartzone.rose_roman.data.reponse.ResponseCategory

class CategoryViewModel(private val homeRepository: HomeRepository, private val schedulerProvider: SchedulerProvider) : BaseViewModel() {
    val categoriesResponse=MutableLiveData<ResponseCategory>()

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


    fun getSubCategories(mainId:String){
        setLoading(true)
        add {
            homeRepository.getSubCategories(mainId).with(schedulerProvider)
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