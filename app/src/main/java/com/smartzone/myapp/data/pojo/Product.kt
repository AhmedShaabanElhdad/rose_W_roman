package com.smartzone.myapp.data.pojo

import java.io.Serializable

data class Product(
    val category_id: String,
    val created: String? = null,
    val description: String? = null,
    val description_ar: String? = null,
    var favourite: Boolean,
    val homepage: String,
    val id: String,
    val image: String? = null,
    val images: List<String>,
    val name: String,
    val name_ar: String,
    val price: String,
    val sale: String="0.0f",
    val status: String,
    var quantity: Int = 1,
    var isAddCart: Boolean = false,
    var color_id: Int = 0,
    var size_id: Int = 0,
    var category: Category? = null,
    val size: List<BasObject>,
    val color: List<BasObject>
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is Product && other.id == this.id) return true
        return false
    }
}

data class BasObject(val id: String, val name: String) : Serializable