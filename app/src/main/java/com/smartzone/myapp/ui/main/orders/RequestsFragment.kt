package com.smartzone.myapp.ui.main.orders

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.reponse.Request
import com.smartzone.diva_wear.databinding.FragmentOrdersBinding
import com.smartzone.myapp.ui.base.BaseFragment
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestsFragment : BaseFragment<FragmentOrdersBinding>() {

    private val viewModel: RequestsViewModel by viewModel()
    lateinit var binding: FragmentOrdersBinding
    private val tabTittle by lazy {
        resources.getStringArray(R.array.tittles_requests)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=getViewDataBinding()
        binding.recycle.layoutManager=LinearLayoutManager(activity)
        binding.tabs.addTab(binding.tabs.newTab().setText(tabTittle[0]))
        binding.tabs.addTab(binding.tabs.newTab().setText(tabTittle[1]))
        observerList()
        viewModel.getSentRrequest()
        binding.notification.setOnClickListener {
            (activity as MainActivity).openNotification()
        }
        binding.tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position==0){
                    viewModel.getSentRrequest()
                }else if (tab!!.position==1){
                    viewModel.getFinishRrequest()
                }
            }

        })
    }

    fun observerList(){
        viewModel.finishRequest.observe(viewLifecycleOwner, Observer {
            binding.recycle.adapter= RequestsAdapter(it){
                goToDetail(it)
            }
        })
        viewModel.sentRequest.observe(viewLifecycleOwner, Observer {
            binding.recycle.adapter= RequestsAdapter(it){
                goToDetail(it)
            }
        })
    }

    private fun goToDetail(request: Request) {
        activity?.let {
            startActivity(RequestDetailsActivity.getIntent(it).apply {
             putExtra("id",request.id)
            })
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_orders
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }
}