package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.Product
import java.io.Serializable

data class Request(
    val address: String,
    val counts: Int,
    val created: String,
    val id: String,
    val latitude: String,
    val longitude: String,
    val notifiied: String,
    val price: Float,
    val products: List<Product>? = null,
    val status: String,
    val user_id: String,
    val Product: Product?,
    val shipment: Int=0
):Serializable