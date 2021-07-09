package com.smartzone.myapp.data.pojo

import java.io.Serializable

class Cart : Serializable {
    var listProduct: ArrayList<Product> = ArrayList()
    var delviry: City? = null
}