package com.smartzone.myapp.di

import com.smartzone.diva_wear.data.repositery.*
import com.smartzone.myapp.data.repositery.*
import org.koin.dsl.module


val repositeryModule= module {
    factory { HomeRepository(get()) }
    factory { ProductRepositery(get()) }
    factory { FavouriteRepositery(get()) }
    factory { AccountRepositery(get()) }
    factory { GeneralRepositery(get()) }
    factory { CarRepositery(get()) }
    factory { RequestRepositery(get()) }
    factory { NotifcationRepositery(get()) }
}