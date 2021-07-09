package com.smartzone.horizon.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ItemCategoryProductViewBinding
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.reponse.Main
import com.smartzone.myapp.ui.dailogs.PleaseRegisterDialog
import com.smartzone.myapp.ui.products.ProductViewModel
import com.smartzone.myapp.ui.products.ProductsActivity
import com.smartzone.myapp.ui.products.ProductsAdapter
import com.smartzone.myapp.ui.products.product_details.ProductDetailsActivity
import com.smartzone.myapp.utilis.*

class HomeAdapter(
    val main: List<Main>,
    val viewModel: ProductViewModel,
    val fragment: HomeFragment
) :
    RecyclerView.Adapter<HomeAdapter.SingleRow>() {

    lateinit var  viewmodel:ProductViewModel
    lateinit var pref:SavePrefs<User>
    lateinit var binding:ItemCategoryProductViewBinding


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        pref = SavePrefs(p0.context.applicationContext, User::class.java)


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context),
            R.layout.item_category_product_view, p0, false
        )


        return SingleRow(
            binding
        )




    }

    override fun getItemCount(): Int {
        return main.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.bind(p1)
    }

    fun notifyProduct(it: Int?) {
        notifyDataSetChanged()
//        binding.empty.adapter!!.itemCount
//        binding.empty.adapter!!.notifyDataSetChanged()
    }


    inner class SingleRow(var view: ItemCategoryProductViewBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {

            val main = main[pos]

            if (pref.getLanguage() == LanguageType.ARABIC.lang)
                view.name.text =main.name_ar
            else
                view.name.text =main.name


            main.category?.takeIf {
                it.isNotEmpty()
            }?.let {
                view.categoryHeader.visibility = View.VISIBLE
                view.recycleCategories.adapter= CategoryAdapter(
                    main.category
                ){
                    view.root.context.startActivity(ProductsActivity.getIntent(view.root.context).apply {
                        putExtra(ID_KEY,it.id)
                        putExtra(CATEGORY_NAME,it.name)
                    })
                }
            }


            view.viewMore.setOnClickListener {
                view.root.context.startActivity(ProductsActivity.getIntent(view.root.context).apply {
                    putExtra(ID_MAIN,main.id)
                    putExtra(CATEGORY_NAME,main.name)
                })
            }

            main.products?.takeIf {
                it.isNotEmpty()
            }?.let {
                view.productHeader.visibility = View.VISIBLE
                view.empty.adapter = ProductsAdapter(main.products, {
                        product->
                    if (!MyApp.getApp().appPreferencesHelper.isLogin()) {
                        PleaseRegisterDialog(view.root.context).show()
                        return@ProductsAdapter
                    }
                    val cart = MyApp.getCart()
                    cart.save()
                    fragment.startActivityForResult(
                        ProductDetailsActivity.getIntent(view.root.context)
                            .apply {
                                putExtra(CATEGORY_ID,product.category_id)
                                putExtra(ID_KEY,product.id)
                            }
                        ,1)
                }, { idProduct: String, position: Int ->
                    if (!MyApp.getApp().appPreferencesHelper.isLogin()) {
                        PleaseRegisterDialog(view.root.context).show()
                        return@ProductsAdapter
                    }
                    viewModel.addFavourite(idProduct, position,main.products[position])
                })
            }


            //AppUtils.loadGlideImage(view.root.context,category.image,R.drawable.default_image,view.categoryImage)

        }
    }


}