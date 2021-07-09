package com.smartzone.myapp.ui.products

import androidx.lifecycle.MutableLiveData
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.Product
import com.smartzone.myapp.data.pojo.User
import com.smartzone.diva_wear.data.repositery.FavouriteRepositery
import com.smartzone.myapp.data.repositery.ProductRepositery
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.utilis.SingleLiveEvent
import com.smartzone.myapp.utilis.rx.SchedulerProvider
import com.smartzone.myapp.utilis.rx.with

class ProductViewModel(
    private val productRepositery: ProductRepositery,
    private val favouriteRepositery: FavouriteRepositery,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val lisProducts = MutableLiveData<List<Product>>()
    val noitfyPosition = SingleLiveEvent<Int>()
    val facourite = SingleLiveEvent<Boolean>()
    val change = SingleLiveEvent<Boolean>()
    val product = MutableLiveData<Product>()

    fun getProducts(categoryId: String, search: String? = null, sort: String? = null) {
        setLoading(true)
        add {
            productRepositery.getProducts(category_id = categoryId, search = search, sort = sort)
                .with(schedulerProvider)
                .subscribe({
                    if (it.status) {
                        setQuentityWithCart(it.products)
                        lisProducts.value = it.products
                    } else {
                        lisProducts.value = ArrayList()
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    fun getSearchProducts(search: String? = null, sort: String? = null) {
        setLoading(true)
        add {
            productRepositery.search(search = search, sort = sort)
                .with(schedulerProvider)
                .subscribe({
                    if (it.status) {
                        setQuentityWithCart(it.products)
                        lisProducts.value = it.products
                    } else {
                        lisProducts.value = ArrayList()
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    fun getMainProduct(categoryId: String, sort: String? = null) {
        setLoading(true)
        add {
            productRepositery.getMainProduct(category_id = categoryId, sort = sort)
                .with(schedulerProvider)
                .subscribe({
                    if (it.status) {
                        setQuentityWithCart(it.products)
                        lisProducts.value = it.products
                    } else {
                        lisProducts.value = ArrayList()
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    fun getProductsSimilar(categoryId: String, idProduct: String) {
        add {
            productRepositery.getProductSimilar(category_id = categoryId, product_id = idProduct)
                .with(schedulerProvider)
                .subscribe({
                    if (it.status) {
                        setQuentityWithCart(it.products)
                        lisProducts.value = it.products
                    } else {
                        lisProducts.value = ArrayList()
                    }
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    private fun setQuentityWithCart(products: List<Product>) {
        val cart = MyApp.getCart()
        for (itm in products) {
            if (cart.orderBean.listProduct.contains(itm)) {
                val index = cart.orderBean.listProduct.indexOf(itm)
                itm.quantity = cart.orderBean.listProduct[index].quantity
            } else {
                itm.quantity = 1
            }
        }
    }

    fun getProductById(idProduct: String) {
        setLoading(true)
        add {
            productRepositery.getProducts(id = idProduct).with(schedulerProvider)
                .subscribe({
                    if (it.status) {
                        setQuentityWithCart(it.products)
                        if (it.products.isNotEmpty())
                            product.value = it.products[0]
                    } else {
                        lisProducts.value = ArrayList()
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    fun addFavourite(idProduct: String, index: Int?, myProduct: Product? = null) {
        setLoading(true)
        val user = SavePrefs(MyApp.getApp(), User::class.java).load()
        add {
            favouriteRepositery.add_favourite(user_id = user?.id!!, id = idProduct)
                .with(schedulerProvider).subscribe({
                    if (it.status && index != null) {
                        change.value = true
                        //this is for add favourite in home page
                        if (myProduct != null) {
                            myProduct.favourite = it.favourite
                            noitfyPosition.value = index
                        }

                        //this is for product details
                        if (index == -1) {
                            facourite.value = it.favourite
                            //product.value!!.favourite = it.favourite
                        } else
                            lisProducts.value?.let { products ->
                                products[index].favourite = it.favourite
                                noitfyPosition.value = index
                            }

                    } else {
                        setErrorMessage("error")
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

    fun getOffer() {
        setLoading(true)
        add {
            productRepositery.getOffers()
                .with(schedulerProvider)
                .subscribe({
                    if (it.status) {
                        setQuentityWithCart(it.products)
                        lisProducts.value = it.products
                    } else {
                        lisProducts.value = ArrayList()
                    }
                    setLoading(false)
                }, {
                    setErrorNetwork(it)
                })
        }
    }

}