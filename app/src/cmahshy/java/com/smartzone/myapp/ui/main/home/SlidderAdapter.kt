package com.smartzone.myapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ItemHomeImageSliderBinding
import com.smartzone.myapp.utilis.AppUtils
import com.smartzone.myapp.data.pojo.Slider

class SlidderAdapter(
    val sliders: List<Slider>,
    val click: (Slider) -> Unit

) :
    RecyclerView.Adapter<SlidderAdapter.SingleRow>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        return SingleRow(
            DataBindingUtil.inflate(LayoutInflater.from(p0.context), R.layout.item_home_image_slider, p0, false)
        )


    }

    override fun getItemCount(): Int {
        return sliders.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemHomeImageSliderBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val category = sliders[pos]
            AppUtils.loadGlideImage(view.root.context,category.image,R.drawable.default_image,view.image)
            view.root.setOnClickListener {
                click(category)
            }

        }
    }


}