package com.smartzone.myapp.ui.register

import java.io.Serializable

data class User(val name: String, val password: String, val phone: String,val idCity: String? = null,val username: String? = null):Serializable