package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.City

data class ResponseCity(
    val cities: List<City>,
    val status: Boolean
)