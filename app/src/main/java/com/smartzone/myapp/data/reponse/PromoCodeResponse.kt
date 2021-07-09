package com.smartzone.myapp.data.reponse

import java.io.Serializable

data class PromoCodeResponse(
    val status: Boolean,
    val price: String,
    val discount: String,
    val id: String,
    val type: String,
    val message: String = "",
    val message_ar: String = ""
) : Serializable