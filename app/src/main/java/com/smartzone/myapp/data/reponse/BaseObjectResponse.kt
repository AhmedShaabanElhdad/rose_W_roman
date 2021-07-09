package com.smartzone.myapp.data.reponse

import java.io.Serializable

data class BaseObjectResponse(
    val status: Boolean,
    val id: String,
    val message: String ="حذث مشكلة"
) : Serializable