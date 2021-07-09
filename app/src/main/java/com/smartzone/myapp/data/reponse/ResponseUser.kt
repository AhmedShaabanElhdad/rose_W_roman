package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.User

data class ResponseUser(
    val status: Boolean,
    val user: User?,
    val message:String?
)