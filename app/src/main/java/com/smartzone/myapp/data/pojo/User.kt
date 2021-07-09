package com.smartzone.myapp.data.pojo

data class User(
    val activated: String,
    val address: String,
    val city_id: String,
//    val cityname: String,
    val code: Any?,
    val created: String,
    val device: String,
    val id: String,
    val image: String?=null,
    val mobile: String,
    val name: String,
    val password: String,
    val role: String,
    val status: String,
    val token: String,
    val updated: String,
    val username: String
)