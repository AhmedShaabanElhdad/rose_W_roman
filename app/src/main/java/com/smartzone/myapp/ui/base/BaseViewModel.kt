package com.smartzone.myapp.ui.base
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartzone.myapp.utilis.SingleLiveEvent

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val errorNetwork = SingleLiveEvent<Throwable>()
    val message = SingleLiveEvent<String>()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun add(job: () -> Disposable) {
        disposables.add(job())
    }


    fun onClear() {
        super.onCleared()
        disposables.clear()
    }


    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }


    fun setErrorMessage(error: String?) {
        setLoading(false)
        if (error == null)
            this.message.value = "error"
        else {
            this.message.value = error
        }
    }

    fun setErrorNetwork(error: Throwable) {
        setLoading(false)
        this.errorNetwork.value = error
    }


}