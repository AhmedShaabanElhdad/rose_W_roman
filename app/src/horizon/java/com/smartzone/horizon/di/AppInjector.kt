package com.smartzone.horizon.di

import com.smartzone.myapp.MyApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@SuppressWarnings("unchecked")
object AppInjector {

    fun init(app: MyApp) {
                startKoin {
            androidLogger(Level.DEBUG)
            androidContext(app)
            modules(listOf(
                com.smartzone.myapp.di.retrofitClient,
                com.smartzone.horizon.di.rxModule,
                com.smartzone.horizon.di.viewModleModule,
                com.smartzone.myapp.di.repositeryModule
            ))
        }
    }

}