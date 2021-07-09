package com.smartzone.myapp.ui.products.product_details


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.Product
import com.smartzone.myapp.data.pojo.User

import com.smartzone.diva_wear.databinding.ActivityProductDetailsBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.dailogs.AddCartDialog
import com.smartzone.myapp.ui.dailogs.PleaseRegisterDialog
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.ui.products.ProductViewModel
import com.smartzone.myapp.ui.products.ProductsAdapterProductDetails
import com.smartzone.myapp.utilis.CATEGORY_ID
import com.smartzone.myapp.utilis.ID_KEY
import com.smartzone.myapp.utilis.LanguageType
import com.smartzone.myapp.utilis.SavePrefs
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsActivity : BaseActivity<ActivityProductDetailsBinding>() {
    val viewModel: ProductViewModel by viewModel()
    lateinit var binding: ActivityProductDetailsBinding
    val idProduct by lazy {
        intent.extras?.getString(ID_KEY, "1")
    }

    //    val idCategory by lazy {
//        intent.extras?.getString(CATEGORY_ID, "1")
//    }
    val cart by lazy {
        MyApp.getCart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        observeProduct()
        binding.recycleProduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.notification.setOnClickListener {
            openNotification()
        }
        viewModel.getProductById(idProduct!!)
        observeProducts()
        binding.cart.setOnClickListener {

            if (!isLoggedin) {
                PleaseRegisterDialog(this).show()
            } else {
                goToCart()
            }

        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_product_details
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    val isLoggedin: Boolean = MyApp.getApp().appPreferencesHelper.isLogin()

    fun addToCart(product: Product) {

        if (!isLoggedin) {
            PleaseRegisterDialog(this).show()
            return
        }
        cart.addProductCart(product)
        cart.save()
        setResult(1)

        binding.countCart.text = "${cart.getCartCount()}"
        AddCartDialog(this).show(clickBuy = {
            goToCart()
        }, clickAnotherProduct = {
            val intent = MainActivity.getIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
    }

    fun observeProduct() {
        viewModel.product.observe(this, Observer { product ->
            viewModel.noitfyPosition.observe(this, Observer {
                binding.recycleProduct.adapter?.notifyItemChanged(it)
            })
            viewModel.getProductsSimilar(product.category_id, idProduct!!)
            binding.addCart.setOnClickListener {
                addToCart(product)
            }

            val imageList = ArrayList<String>()
            imageList.add(product.image!!)
            imageList.addAll(product.images)
            binding.countCart.text = "${cart.getCartCount()}"
//            if (product.images.isNotEmpty()) {
            binding.pager.adapter = SlidderProductAdapter(imageList)
            TabLayoutMediator(binding.tabLayoutDots, binding.pager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
            }.attach()
//            }

            if (SavePrefs(this, User::class.java).getLanguage().equals(LanguageType.ARABIC.lang)) {
                binding.nameOfProduct.text = product.name_ar
                binding.description.text = product.description_ar
            } else {
                binding.nameOfProduct.text = product.name
                binding.description.text = product.description
            }

            binding.count.text = "${product.quantity}"
            if (product.favourite) {
                binding.like.setImageResource(R.drawable.likeactive)
            } else {
                binding.like.setImageResource(R.drawable.like)
            }
            if (product.sale.toFloat() != 0.0f) {
                binding.priceSalled.text = product.sale
                binding.priceSalled.visibility = View.VISIBLE
                binding.price.text = product.price
                binding.price.background =
                    ContextCompat.getDrawable(this, R.drawable.line_drawaable)
            } else {
                binding.price.text = product.price
                binding.priceSalled.visibility = View.GONE
                binding.price.background = null
            }
            val prod = cart.getProductFromCart(idProduct!!)

            binding.plus.setOnClickListener {
                binding.count.text = "${cart.addproduct(product)}"
                // binding.countCart.text="${cart.orderBean.listProduct.size}"
            }
            binding.minus.setOnClickListener {
                binding.count.text = "${cart.removeProduct(product)}"
                // binding.countCart.text="${ca}"
            }

            binding.like.setOnClickListener {
                if (!MyApp.getApp().appPreferencesHelper.isLogin())
                    PleaseRegisterDialog(it.context).show()
                else
                    viewModel.addFavourite(idProduct!!, -1)
            }
            binding.like.bringToFront()
        })
        viewModel.change.observe(this, Observer {
            setResult(1)
        })
        viewModel.facourite.observe(this, Observer {

            if (it) {
                binding.like.setImageResource(R.drawable.likeactive)
            } else {
                binding.like.setImageResource(R.drawable.like)
            }
        })

    }

    private fun goToCart() {
        startActivity(
            MainActivity.getIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("cart", true)
        )
    }

    fun observeProducts() {
        viewModel.lisProducts.observe(this, Observer {
            binding.recycleProduct.adapter = ProductsAdapterProductDetails(it, { product ->
                val cart = MyApp.getCart()
                cart.save()
                startActivity(
                    getIntent(this)
                    .apply {
                        putExtra(CATEGORY_ID, product.category_id)
                        putExtra(ID_KEY, product.id)
                    }
                )
                finish()
            }, { idProduct: String, position: Int ->
                viewModel.addFavourite(idProduct, position)
            })
        })
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, ProductDetailsActivity::class.java)
    }

}