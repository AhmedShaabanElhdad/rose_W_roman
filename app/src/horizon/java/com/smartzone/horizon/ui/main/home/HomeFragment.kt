package com.smartzone.horizon.ui.main.home

import android.icu.util.UniversalTimeScale.MAX_SCALE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.FragmentHomeBinding
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.Slider
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.ui.base.BaseFragment
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.ui.order_details.details.OrderDetailsViewModel
import com.smartzone.myapp.ui.products.ProductViewModel
import com.smartzone.myapp.ui.products.ProductsActivity
import com.smartzone.myapp.ui.products.product_details.ProductDetailsActivity
import com.smartzone.myapp.utilis.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() {


    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel2 by viewModel()
    private val productViewModel: ProductViewModel by viewModel()
    private val cityViewModel: OrderDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
//        binding.pager.clipToPadding = false
//        binding.pager.pageMargin = ViewUtils.dpToPx(10.0f)

        //binding.pager.setPageTransformer(false,transformer)
//        binding.recycleCategories.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        viewModel.loadData()
        viewModel.loadLink()
        viewModel.getCategories()
        viewModel.getSlider()
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

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    fun observeSlider() {
        viewModel.slider.observe(viewLifecycleOwner, Observer { sliders ->
            activity?.let {
                if (sliders.isNotEmpty())
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
                TabLayoutMediator(binding.tabLayoutDots, binding.pager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
                }.attach()
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
        observeMainResponse()
        observeSlider()
    }

    fun observeMainResponse() {

        viewModel.mainResponse.observe(viewLifecycleOwner, Observer {
            binding.recycleMain.adapter = HomeAdapter(it.main, productViewModel, this)
        })
        viewModel.linkResponse.observe(viewLifecycleOwner, Observer {
            binding.recyclelink.adapter = HomeAdapter(it.main, productViewModel, this)
        })

        productViewModel.noitfyPosition.observe(viewLifecycleOwner, Observer {
            (binding.recyclelink.adapter as HomeAdapter).notifyProduct(it)
            (binding.recycleMain.adapter as HomeAdapter).notifyProduct(it)
        })

        observeCategoriesResponse()
    }


    private fun observeCategoriesResponse() {
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { response ->


            binding.recycleCat.adapter = CategoryAdapter(response.categories) {
                startActivity(ProductsActivity.getIntent(requireContext()).apply {
                    putExtra(ID_KEY,it.id)
                    putExtra(CATEGORY_NAME,it.name)
                })
            }
        })

    }

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