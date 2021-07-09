package com.smartzone.myapp.ui.confirm_order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.reponse.PromoCodeResponse
import com.smartzone.myapp.data.repositery.CarRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class ConfirmOrderDetailsViewModel(
    private val cartRepositery: CarRepositery,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    val success = MutableLiveData<Boolean>()
    val id = MutableLiveData<String>()
    val checkPromo = MutableLiveData<PromoCodeResponse>()

    fun addCart(promocode: String) {
        val cart = MyApp.getCart()
        val map = HashMap<String, String>()
        val list = cart.getCartList()
        for ((index, value) in list.withIndex()) {
            map["product_id[$index]"] = "${value.id}"
            Log.e("TagValue", "")
            map["quantity[$index]"] = "${value.quantity}"
            map["size_id[$index]"] = "${value.size_id}"
            map["color_id[$index]"] = "${value.color_id}"
        }
        setLoading(true)
        val user = SavePrefs(MyApp.getApp(), User::class.java).load()
        add {
            cartRepositery.add_cart(user?.id!!, cart.orderBean.delviry!!.id, promocode, map)
                .with(schedulerProvider)
//            cartRepositery.add_cart("1",cart.orderBean.delviry!!.id,promocode,map).with(schedulerProvider)
                .subscribe({
                    if (it.status) {
                        id.value = it.id
                        success.value = true
                    } else {
                        setErrorMessage(it.message)
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    fun checkPromoCode(promocode: String, totalPrice: String) {

        val pref = SavePrefs(MyApp.getApp(), User::class.java)

        setLoading(true)
        val user = pref.load()
        add {
//            cartRepositery.checkPromoCode(total_price = totalPrice,promocode = promocode,user_id = "1").with(schedulerProvider)
            cartRepositery.checkPromoCode(
                total_price = totalPrice,
                promocode = promocode,
                user_id = user?.id!!
            ).with(schedulerProvider)
                .subscribe({
                    checkPromo.value = it
                    if (!it.status) {
                        if (pref.getLanguage().equals("ar"))
                            setErrorMessage(it.message_ar)
                        else
                            setErrorMessage(it.message)
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

}