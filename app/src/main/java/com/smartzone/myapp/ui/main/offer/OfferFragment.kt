package com.smartzone.myapp.ui.main.offer

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.OfferFragmentBinding
import com.smartzone.myapp.ui.base.BaseFragment
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.ui.products.ProductViewModel
import com.smartzone.myapp.ui.products.product_details.ProductDetailsActivity
import com.smartzone.myapp.ui.products.ProductsAdapter
import com.smartzone.myapp.utilis.CATEGORY_ID
import com.smartzone.myapp.utilis.ID_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class OfferFragment : BaseFragment<OfferFragmentBinding>() {

    companion object {
        fun newInstance() = OfferFragment()
    }
    lateinit var binding: OfferFragmentBinding
    val viewModel: ProductViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=getViewDataBinding()
        binding.recycle.layoutManager=GridLayoutManager(activity,2)

        viewModel.getOffer()

        observeListProducts()
        observeNotify()
        binding.notification.setOnClickListener {
            (activity as MainActivity).openNotification()
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
        return R.layout.offer_fragment
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }



}