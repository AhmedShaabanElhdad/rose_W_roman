package com.smartzone.myapp.ui.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.City
import com.smartzone.diva_wear.databinding.ItemSelectCityBinding

class CityAdapter(
    val cities: List<City>,
    val click:(City)->Unit
) :
    RecyclerView.Adapter<CityAdapter.SingleRow>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        return SingleRow(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.item_select_city, p0, false
            )
        )


    }

    override fun getItemCount(): Int {
        return cities.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemSelectCityBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val city = cities[pos]
            view.txtName.text=city.name
            view.root.setOnClickListener {
                click(city)
            }
        }
    }


}