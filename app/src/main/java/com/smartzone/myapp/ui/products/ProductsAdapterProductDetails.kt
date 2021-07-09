package com.smartzone.myapp.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.Product
import com.smartzone.myapp.data.pojo.User
import com.smartzone.diva_wear.databinding.ItemProductSimilarBinding
import com.smartzone.myapp.utilis.AppUtils
import com.smartzone.myapp.utilis.LanguageType
import com.smartzone.myapp.utilis.SavePrefs

class ProductsAdapterProductDetails(
    val products: List<Product>,
    val click: (Product) -> Unit,
    val clickFavourite: (String,Int)->Unit
) :
    RecyclerView.Adapter<ProductsAdapterProductDetails.SingleRow>() {

    val cartManager= MyApp.getCart()
    lateinit var pref: SavePrefs<User>

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        pref = SavePrefs(p0.context, User::class.java)
        return SingleRow(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.item_product_similar, p0, false
            )
        )


    }

    override fun getItemCount(): Int {
        return products.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemProductSimilarBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val product = products[pos]
            if (pref.getLanguage().equals(LanguageType.ARABIC.lang))
                view.nameOfProduct.text = product.name_ar
            else
                view.nameOfProduct.text = product.name
            view.price.text = product.price
            view.count.text="${product.quantity}"
            product.image?.let {
                AppUtils.loadGlideImage(
                    view.root.context,
                    it,
                    R.drawable.default_image,
                    view.imageProduct
                )
            }
            if (product.favourite)
                view.like.setImageResource(R.drawable.likee)
            else
                view.like.setImageResource(R.drawable.likeeee)
            view.root.setOnClickListener {
                click(product)
            }

            if(product.sale.toFloat() != 0.0f){
                view.priceSalled.text=product.sale
                view.priceSalled.visibility= View.VISIBLE
                view.price.text=product.price
                view.price.background= ContextCompat.getDrawable(view.root.context,R.drawable.line_drawaable)
            }else{
                view.price.text=product.price
                view.priceSalled.visibility= View.GONE
                view.price.background=null
            }
            view.cart.setOnClickListener {
                if (cartManager.addProductCart(product)){
                    Toast.makeText(view.root.context,view.root.context.getString(R.string.doneAddCart),Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(view.root.context,view.root.context.getString(R.string.isAlerdyExsist),Toast.LENGTH_LONG).show()
                }
                notifyItemChanged(layoutPosition)

            }
            view.plus.setOnClickListener {
                product.quantity=cartManager.addproduct(product)
                notifyItemChanged(layoutPosition)
            }
            view.minus.setOnClickListener {
                product.quantity=cartManager.removeProduct(product)
                notifyItemChanged(layoutPosition)
            }
            view.like.setOnClickListener {
                clickFavourite(product.id,layoutPosition)
            }

        }
    }


}