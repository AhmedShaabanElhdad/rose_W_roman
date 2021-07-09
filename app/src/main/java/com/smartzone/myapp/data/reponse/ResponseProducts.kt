package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.Product

data class ResponseProducts(
    val count: Int,
    val products: List<Product>,
    val status: Boolean
)