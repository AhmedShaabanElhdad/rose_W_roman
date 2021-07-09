package com.smartzone.myapp.ui.confirm_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.Product
import com.smartzone.diva_wear.databinding.ItemProductConfirmBinding
import com.smartzone.myapp.utilis.AppUtils

class OrderAdapter(
    val cart: List<Product>
) :
    RecyclerView.Adapter<OrderAdapter.SingleRow>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        return SingleRow(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.item_product_confirm, p0, false
            )
        )


    }

    override fun getItemCount(): Int {
        return cart.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemProductConfirmBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val product = cart[pos]
            view.tvName.text = product.name_ar
            if (product.sale.toFloat() != 0.0f) {
                view.priceSalled.text = product.sale
                view.priceSalled.visibility = View.VISIBLE
                view.price.text = product.price
                view.price.background =
                    ContextCompat.getDrawable(view.root.context, R.drawable.line_drawaable)
            } else {
                view.price.text = product.price
                view.priceSalled.visibility = View.GONE
                view.price.background = null
            }
            view.quantity.text = "${product.quantity}"
            AppUtils.loadGlideImage(
                view.root.context,
                product.image,
                R.drawable.default_image,
                view.imageProduct
            )
        }
    }


}