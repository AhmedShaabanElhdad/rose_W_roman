package com.smartzone.myapp.di

//import com.smartzone.horizon.ui.main.home.HomeViewModel2
//import com.smartzone.horizon.ui.main.prerequest.PrerequestViewModel
import com.smartzone.myapp.ui.confirm_order.ConfirmOrderDetailsViewModel
import com.smartzone.myapp.ui.contact_us.ContactusViewModel
import com.smartzone.myapp.ui.forget_password.ForgetPassordViewModel
import com.smartzone.myapp.ui.login.LoginViewModel
import com.smartzone.myapp.ui.main.favourite.FavouriteViewModel
import com.smartzone.myapp.ui.change_password.ChangePasswordViewModel
//import com.smartzone.rose_roman.ui.main.home.HomeViewModel
import com.smartzone.myapp.ui.main.orders.RequestsViewModel
import com.smartzone.myapp.ui.notification.NotficationViewModel
import com.smartzone.myapp.ui.order_details.address.OrderAddressesViewModel
import com.smartzone.myapp.ui.order_details.details.OrderDetailsViewModel
import com.smartzone.myapp.ui.products.ProductViewModel
import com.smartzone.myapp.ui.profile.ProfileViewModel
import com.smartzone.myapp.ui.register.RegisterViewModel
import com.smartzone.myapp.utilis.rx.ApplicationSchedulerProvider
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.sa3d.ui.main.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModleModule= module {
//    viewModel { HomeViewModel2(get(),get()) }
//    viewModel { PrerequestViewModel(get(),get()) }
    viewModel { ProductViewModel(get(),get(),get()) }
    viewModel { HomeViewModel(get(),get()) }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { RegisterViewModel(get(),get(),get()) }
    viewModel { ForgetPassordViewModel(get(),get()) }
    viewModel { OrderDetailsViewModel(get(),get()) }
    viewModel { ConfirmOrderDetailsViewModel(get(),get()) }
    viewModel { RequestsViewModel(get(),get()) }
    viewModel { FavouriteViewModel(get(),get()) }
    viewModel { NotficationViewModel(get(),get()) }
    viewModel { ProfileViewModel(get(),get(),get()) }
    viewModel { ContactusViewModel(get(),get()) }
    viewModel { OrderAddressesViewModel(get(),get()) }
    viewModel { ChangePasswordViewModel(get(),get(),get()) }
//    viewModel { CategoryViewModel(get(),get()) }


}
val rxModule = module {
    factory { ApplicationSchedulerProvider() as SchedulerProvider }
}
