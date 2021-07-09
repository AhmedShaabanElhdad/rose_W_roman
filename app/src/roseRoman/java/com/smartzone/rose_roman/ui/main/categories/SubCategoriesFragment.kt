package com.smartzone.rose_roman.ui.main.categories

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.michael.easydialog.EasyDialog
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.FragmentSubCategoriesBinding
import com.smartzone.rose_roman.MyApp
import com.smartzone.rose_roman.data.pojo.Category
import com.smartzone.rose_roman.ui.base.BaseFragment
import com.smartzone.rose_roman.ui.base.BaseViewModel
import com.smartzone.horizon.ui.main.home.CategoryAdapter
import com.smartzone.rose_roman.ui.products.ProductViewModel
import com.smartzone.rose_roman.ui.products.ProductsAdapter
import com.smartzone.rose_roman.ui.products.product_details.ProductDetailsActivity
import com.smartzone.rose_roman.utilis.CATEGORY_ID
import com.smartzone.rose_roman.utilis.ID_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel


class SubCategoriesFragment : BaseFragment<FragmentSubCategoriesBinding>() {


    lateinit var binding: FragmentSubCategoriesBinding
    private val viewModel: CategoryViewModel by viewModel()
    private val productViewModel: ProductViewModel by viewModel()


    var sortType = -1
    var mainId = "-1"
    var selectedCategoryId = "-1"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()

        arguments?.let {
            val safeArgs = SubCategoriesFragmentArgs.fromBundle(it)
            mainId = safeArgs.mainId
            selectedCategoryId = mainId
        }


        viewModel.getSubCategories(mainId)
        productViewModel.getProducts(mainId)

        binding.sortContent.setOnClickListener {
            showPopoMenu()
        }

        observeCategoriesResponse()

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sub_categories
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private fun observeCategoriesResponse() {
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { response ->
            val newList = ArrayList<Category>(response.categories)
            newList.add(0, Category(mainId, "", getString(R.string.all), getString(R.string.all)))

            binding.recycleCat.adapter = CategoryAdapter(newList) {
                selectedCategoryId = it.id
                refresh()
            }
        })

        productViewModel.lisProducts.observe(viewLifecycleOwner, Observer {
            binding.recycleProducts.adapter = ProductsAdapter(it, { product ->
                val cart = MyApp.getCart()
                cart.save()
                startActivityForResult(
                    ProductDetailsActivity.getIntent(requireContext())
                        .apply {
                            putExtra(CATEGORY_ID, product.category_id)
                            putExtra(ID_KEY, product.id)
                        }, 1
                )
            }, { idProduct: String, position: Int ->
                viewModel.setLoading(true)
                productViewModel.addFavourite(idProduct, position)
            })

            viewModel.setLoading(false)
        })

        productViewModel.noitfyPosition.observe(viewLifecycleOwner, Observer {
            viewModel.setLoading(false)
            binding.recycleProducts.adapter?.notifyItemChanged(it)
        })
    }


    fun showPopoMenu() {
        val view: View =
            this.layoutInflater.inflate(R.layout.sort_layout, null)
        val itemHighPrice = view.findViewById<TextView>(R.id.highPrice)
        val itemLowPrice = view.findViewById<TextView>(R.id.lowPrice)
        if (sortType == 1) {
            itemHighPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            itemLowPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        } else if (sortType == 2) {
            itemHighPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            itemLowPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        val easyDialog =
            EasyDialog(requireContext()) // .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
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
        } else if (sortType == 2) {
            "high"
        } else
            null

        viewModel.setLoading(true)
        productViewModel.getProducts(categoryId = selectedCategoryId, sort = sort)
    }
}