package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.Notification

data class ResponseNotification(
    val count: Int,
    val notifications: List<Notification>,
    val status: Boolean
)