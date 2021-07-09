package com.smartzone.myapp.ui.main.favourite

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.User
import com.smartzone.diva_wear.databinding.FavouriteFragmentBinding
import com.smartzone.myapp.ui.base.BaseFragment
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.ui.products.product_details.ProductDetailsActivity
import com.smartzone.myapp.ui.products.ProductsAdapter
import com.smartzone.myapp.utilis.CATEGORY_ID
import com.smartzone.myapp.utilis.ID_KEY
import com.smartzone.myapp.utilis.SavePrefs
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : BaseFragment<FavouriteFragmentBinding>() {

    companion object {
        fun newInstance() = FavouriteFragment()
    }
    lateinit var binding: FavouriteFragmentBinding
    val viewModel: FavouriteViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=getViewDataBinding()
        binding.recycle.layoutManager=GridLayoutManager(activity,2)
        observeListProducts()
        observeNotify()
        refresh()
        binding.notification.setOnClickListener {
            (activity as MainActivity).openNotification()
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
    }



    fun observeListProducts() {
        viewModel.lisProducts.observe(viewLifecycleOwner, Observer {
            binding.recycle.adapter = ProductsAdapter(it, {
                    product->
                val cart = MyApp.getCart()
                cart.save()
                activity?.let {
                    startActivityForResult(
                        ProductDetailsActivity.getIntent(it)
                            .apply {
                                putExtra(CATEGORY_ID,product.category_id)
                                putExtra(ID_KEY,product.id)
                            }
                        ,1)
                }
            }, { idProduct: String, position: Int ->
                viewModel.addFavourite(idProduct, position)
            })
        })
    }
    fun observeNotify() {
        viewModel.noitfyPosition.observe(this, Observer {
            val list=viewModel.lisProducts.value!!.toMutableList()
            list.removeAt(it)
            viewModel.lisProducts.value=list
            binding.recycle.adapter?.notifyDataSetChanged()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun getLayoutId(): Int {
        return R.layout.favourite_fragment
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    fun refresh() {
        val user= SavePrefs(MyApp.getApp(), User::class.java).load()

        if (TextUtils.isEmpty(binding.etSearch.text.toString()))
            viewModel.getFavourite(user!!.id)
        else
            viewModel.getFavourite(user!!.id,binding.etSearch.text.toString())
    }


}