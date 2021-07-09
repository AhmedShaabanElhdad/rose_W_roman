package com.smartzone.myapp.data.reponse

import com.smartzone.myapp.data.pojo.Category
import com.smartzone.myapp.data.pojo.Product

data class Main(
    val banner: String,
    val category: List<Category>?,
    val created: String,
    val homepage: String,
    val id: String,
    val image: String,
    val image_mob: String,
    val name: String,
    val name_ar: String,
    val products: List<Product>?,
    val status: String
)