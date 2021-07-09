package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.Address

data class AddressResponse(
    val list: List<Address>,
    val status: Boolean
)