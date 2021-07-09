package com.smartzone.myapp.ui.products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.michael.easydialog.EasyDialog
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityProductsBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.products.product_details.ProductDetailsActivity
import com.smartzone.myapp.utilis.CATEGORY_ID
import com.smartzone.myapp.utilis.ID_KEY
import com.smartzone.myapp.utilis.ID_MAIN
import com.smartzone.myapp.utilis.ID_SEARCH
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductsActivity : BaseActivity<ActivityProductsBinding>() {

    val viewModel: ProductViewModel by viewModel()
    lateinit var binding: ActivityProductsBinding
    lateinit var categoryId: String
    lateinit var mainCategoryId: String
    lateinit var search: String
    var sortType = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        //binding.contentToolbar.tittle.text = intent.extras?.getString(CATEGORY_NAME)
        binding.empty.layoutManager = GridLayoutManager(this, 2)
        categoryId = intent.extras?.getString(ID_KEY, "-1")!!
        mainCategoryId = intent.extras?.getString(ID_MAIN, "-1")!!
        search = intent.extras?.getString(ID_SEARCH, "")!!
        if (search.isNotEmpty())
            viewModel.getSearchProducts(search)
        else {
            if (mainCategoryId.equals("-1"))
                viewModel.getProducts(categoryId)
            else
                viewModel.getMainProduct(mainCategoryId)
        }
        binding.contentToolbar.back.setOnClickListener {
            onBackPressed()
        }
        binding.sortContent.setOnClickListener {
            showPopoMenu()
        }
        binding.contentToolbar.notification.setOnClickListener {
            openNotification()
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                refresh()
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.search.setOnClickListener {
            refresh()
            hideKeyboard()
        }
        observeListProducts()
        observeNotify()
    }


    fun observeListProducts() {
        viewModel.lisProducts.observe(this, Observer {
            binding.empty.adapter = ProductsAdapter(it, { product ->
                val cart = MyApp.getCart()
                cart.save()
                startActivityForResult(
                    ProductDetailsActivity.getIntent(this)
                        .apply {
                            putExtra(CATEGORY_ID, product.category_id)
                            putExtra(ID_KEY, product.id)
                        }, 1
                )
            }, { idProduct: String, position: Int ->
                viewModel.addFavourite(idProduct, position)
            })
        })
    }

    fun observeNotify() {
        viewModel.noitfyPosition.observe(this, Observer {
            binding.empty.adapter?.notifyItemChanged(it)
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_products
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onBackPressed() {
        val cart = MyApp.getCart()
        cart.save()
        super.onBackPressed()
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, ProductsActivity::class.java)
    }

    fun showPopoMenu() {
        val view: View =
            this.layoutInflater.inflate(R.layout.sort_layout, null)
        val itemHighPrice = view.findViewById<TextView>(R.id.highPrice)
        val itemLowPrice = view.findViewById<TextView>(R.id.lowPrice)
        if (sortType == 1) {
            itemHighPrice.setTextColor(ContextCompat.getColor(this, R.color.black))
            itemLowPrice.setTextColor(ContextCompat.getColor(this, R.color.red))
        } else if (sortType == 2) {
            itemHighPrice.setTextColor(ContextCompat.getColor(this, R.color.red))
            itemLowPrice.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        val easyDialog =
            EasyDialog(this) // .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                .setLayout(view)
                .setBackgroundColor(
                    this.resources.getColor(R.color.white_grey)
                ) // .setLocation(new location[])//point in screen
                .setLocationByAttachedView(binding.sortContent)
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setTouchOutsideDismiss(true)
                .setAnimationTranslationShow(
                    EasyDialog.DIRECTION_X,
                    350,
                    -600f,
                    100f,
                    -50f,
                    50f,
                    0f
                )
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 350, -50f, 800f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMarginLeftAndRight(24, 24)
                .setMarginLeftAndRight(24, 24)
        itemHighPrice.setOnClickListener {
            easyDialog.dismiss()
            sortType = 2
            refresh()
        }
        itemLowPrice.setOnClickListener {
            sortType = 1
            easyDialog.dismiss()
            refresh()
        }
        easyDialog.show()
    }

    fun refresh() {
        val sort = if (sortType == 1) {
            "low"
        } else if (sortType == 2){
            "high"
        }else
            null

        search = binding.etSearch.text.toString()

        if (TextUtils.isEmpty(binding.etSearch.text.toString())) {
            if (!categoryId.equals("-1"))
                viewModel.getProducts(categoryId, null, sort)
            else
                viewModel.getSearchProducts(binding.etSearch.text.toString(), sort)
        }
        else {
            if (search.isNotEmpty())
                viewModel.getSearchProducts(search, sort)
            else {
                if (mainCategoryId.equals("-1"))
                    viewModel.getProducts(categoryId, sort)
                else
                    viewModel.getMainProduct(mainCategoryId, sort)
            }
//            if (mainCategoryId.equals("-1"))
//                viewModel.getProducts(categoryId, binding.etSearch.text.toString(), sort)
//            else
//                viewModel.getMainProduct(mainCategoryId, sort)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode==1) {
            refresh()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}