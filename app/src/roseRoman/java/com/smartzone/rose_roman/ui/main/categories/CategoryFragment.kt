package com.smartzone.rose_roman.ui.main.categories

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.FragmentCategoriesBinding
import com.smartzone.rose_roman.ui.base.BaseFragment
import com.smartzone.rose_roman.ui.base.BaseViewModel
import com.smartzone.rose_roman.ui.main.MainActivity
import com.smartzone.horizon.ui.main.home.CategoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment : BaseFragment<FragmentCategoriesBinding>() {


    lateinit var binding: FragmentCategoriesBinding
    val viewModel: CategoryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=getViewDataBinding()

        viewModel.getCategories()

        observeCategories()
        binding.notification.setOnClickListener {
            (activity as MainActivity).openNotification()
        }
    }



    fun observeCategories() {
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer {
            binding.recycleCategories.adapter = CategoryAdapter(it.categories,
                CategoryAdapter.VIEW_IMAGE) { category ->
                val action = CategoryFragmentDirections.actionNavigationCategoriesToNavigationSubcategories(category.id)
                findNavController().navigate(action)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_categories
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }



}