package com.smartzone.myapp.data.reponse

data class RequestResponse(
    val count: Int,
    val requests: List<Request>,
    val status: Boolean
)