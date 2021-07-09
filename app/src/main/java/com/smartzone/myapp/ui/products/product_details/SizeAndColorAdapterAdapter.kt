package com.smartzone.myapp.ui.products.product_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.BasObject
import com.smartzone.diva_wear.databinding.ItemSelectCityBinding

class SizeAndColorAdapterAdapter(
    val list: List<BasObject>,
    val click:(BasObject)->Unit
) :
    RecyclerView.Adapter<SizeAndColorAdapterAdapter.SingleRow>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        return SingleRow(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.item_select_city, p0, false
            )
        )


    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemSelectCityBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val city = list[pos]
            view.txtName.text=city.name
            view.root.setOnClickListener {
                click(city)
            }
        }
    }


}