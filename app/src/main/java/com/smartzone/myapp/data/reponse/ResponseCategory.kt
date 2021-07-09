package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.Category
import java.io.Serializable

data class ResponseCategory(val categories: List<Category>, val count: Int,
                            val status: Boolean):Serializable