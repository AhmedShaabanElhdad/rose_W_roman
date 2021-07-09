package com.smartzone.myapp.ui.main.home

import android.icu.util.UniversalTimeScale.MAX_SCALE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.michael.easydialog.EasyDialog
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.FragmentHomeBinding
import com.smartzone.myapp.ui.base.BaseFragment
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.ui.order_details.details.OrderDetailsViewModel
import com.smartzone.myapp.ui.products.ProductViewModel
import com.smartzone.myapp.ui.products.ProductsActivity
import com.smartzone.myapp.ui.products.ProductsAdapter
import com.smartzone.myapp.ui.products.product_details.ProductDetailsActivity
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.utilis.CATEGORY_ID
import com.smartzone.myapp.utilis.ID_SEARCH
import com.smartzone.myapp.utilis.ID_KEY
import com.smartzone.myapp.utilis.ViewUtils
import com.smartzone.myapp.data.pojo.Category
import com.smartzone.myapp.data.pojo.Slider
import com.smartzone.myapp.data.pojo.User
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() {


    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private val productViewModel: ProductViewModel by viewModel()
    private val cityViewModel: OrderDetailsViewModel by viewModel()


    var discount: Float = 0.0f
    var tax = 0.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
//        binding.pager.clipToPadding = false
//        binding.pager.pageMargin = ViewUtils.dpToPx(10.0f)

        //binding.pager.setPageTransformer(false,transformer)
//        binding.recycleCategories.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        viewModel.getCategories()
        cityViewModel.getCity(showingLoader = false)

        binding.notification.setOnClickListener {
            (activity as MainActivity).openNotification()
        }

        binding.etSearch.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                goToSearchProducts(view.text.toString())
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.search.setOnClickListener {

            hideKeyboard()
        }

        binding.sortContent.setOnClickListener {
            showPopoMenu()
        }

    }


    var sortType = -1

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


        if (selectedCategoryId.equals("-1")) {
            productViewModel.getSearchProducts(sort = sort)
        } else {
            productViewModel.getProducts(categoryId = selectedCategoryId, sort = sort)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    fun observeSliderAndCity() {
        viewModel.slider.observe(viewLifecycleOwner, Observer { sliders ->
            activity?.let {
                if (sliders.size > 0)
                    binding.sliderLayout.visibility = View.VISIBLE
                binding.pager.adapter = SlidderAdapter(sliders as ArrayList<Slider>) { slider ->
                    if (slider.type == "1") {  // open product detail
                        activity?.let {
                            startActivity(ProductDetailsActivity.getIntent(it).apply {
                                putExtra(ID_KEY, slider.target)
                            })
                        }
                    } else if (slider.type == "2") {  // open products for category
                        activity?.let {
                            startActivity(ProductsActivity.getIntent(it).apply {
                                putExtra(ID_KEY, slider.target)
                            })
                        }
                    }
                }
//                TabLayoutMediator(binding.tabLayoutDots, binding.pager) { tab, position ->
////            tab.text = "OBJECT ${(position + 1)}"
//                }.attach()
            }
        })


        cityViewModel.cities.observe(viewLifecycleOwner, Observer {
            var cityId = "-1"
            SavePrefs(MyApp.getApp(), User::class.java).load()?.apply {
                cityId = this.city_id
                MyApp.getCart().orderBean.delviry = it.filter { it.id.equals(cityId) }.first()
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getSlider()
        observeCategoriesResponse()
        observeSliderAndCity()
    }

    var selectedCategoryId = "-1"
    private fun observeCategoriesResponse() {
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { response ->

            productViewModel.getSearchProducts()

            val newList = ArrayList<Category>(response.categories)
            newList.add(0, Category("-1", "", getString(R.string.all), getString(R.string.all)))

            binding.recycleCat.adapter = CategoryAdapter(newList) {
                if (it.id.equals("-1")) {
                    productViewModel.getSearchProducts()
                    viewModel.getSlider()
                } else {
                    productViewModel.getProducts(it.id)
                    viewModel.slider.value
                    viewModel.slider.value?.takeIf { it.size > 0 }?.get(0)?.image = it.image
                    binding.pager.adapter?.notifyItemChanged(0)
                }
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

        })

        productViewModel.noitfyPosition.observe(viewLifecycleOwner, Observer {
            viewModel.setLoading(false)
            binding.recycleProducts.adapter?.notifyItemChanged(it)
        })
    }

    //    fun observeMainResponse(){
//        viewModel.mainResponse.observe(viewLifecycleOwner, Observer {
//            binding.recycleMain.adapter=HomeAdapter(it.main,productViewModel,this)
//        })
//
//        productViewModel.noitfyPosition.observe(viewLifecycleOwner, Observer {
//            (binding.recycleMain.adapter as HomeAdapter).notifyProduct(it)
//        })
//    }
    private var transformer: ViewPager.PageTransformer =
        ViewPager.PageTransformer { page, position ->
            val pagerWidthPx = (page.parent as ViewPager).width.toFloat()
            val pageWidthPx: Float = pagerWidthPx - 2 * ViewUtils.dpToPx(5f)
            val maxVisiblePages = pagerWidthPx / pageWidthPx
            val center = maxVisiblePages / 2f
            val scale: Float
            if (position + 0.5f < center - 0.5f || position > center) {
                scale = 0.8f
            } else {
                val coef: Float = if (position + 0.5f < center) {
                    (position + 1 - center) / 0.5f
                } else {
                    (center - position) / 0.5f
                }
                scale = coef * (MAX_SCALE - 0.8f + 0.8f)
            }
            page.scaleX = scale
            page.scaleY = scale
        }


    private fun goToSearchProducts(search: String) {
        startActivity(ProductsActivity.getIntent(requireContext()).apply {
            putExtra(ID_SEARCH, search)
        })
    }
}