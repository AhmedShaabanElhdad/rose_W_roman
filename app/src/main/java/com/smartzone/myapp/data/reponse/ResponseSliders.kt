package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.Slider

data class ResponseSliders(
    val count: Int,
    val sliders: List<Slider>,
    val status: Boolean
)