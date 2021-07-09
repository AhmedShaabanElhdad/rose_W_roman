package com.smartzone.myapp.ui.register

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.data.pojo.City
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.repositery.AccountRepositery
import com.smartzone.myapp.data.repositery.GeneralRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class RegisterViewModel(
    private val accountRepositery: AccountRepositery,
    private val schedulerProvider: SchedulerProvider,
    private val generalRepositery: GeneralRepositery
) : BaseViewModel() {

    val user = MutableLiveData<User>()
    val cities = MutableLiveData<List<City>>()
    fun signUP(
        mobile: String,
        password: String,
        token: String?,
        name: String,
        city_id: String?,
        email: String?
    ) {
        setLoading(true)
        add {
            accountRepositery.register(mobile, password, null, name, city_id, email)
                .with(schedulerProvider).subscribe({
                    if (it.status) {
                        user.value = it.user
                    } else
                        setErrorMessage(it.message)
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    fun getCity() {
        setLoading(true)
        add {
            generalRepositery.getCity()
                .with(schedulerProvider).subscribe({
                    if (it.status) {
                        cities.value = it.cities
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }


    fun checkInput(userName: String, mobile: String) {
        setLoading(true)
        add {
            accountRepositery.validateUserInput(userName, mobile)
                .with(schedulerProvider).subscribe({
                    if (it.status) {
                        user.value = it.user
                    } else {
                        setErrorMessage(it.message)
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }
}