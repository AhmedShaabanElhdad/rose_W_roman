package com.smartzone.myapp.ui.products.product_details

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ItemImageSliderBinding
import com.smartzone.myapp.utilis.AppUtils

class SlidderProductAdapter(
    val pathes: ArrayList<String>

) :
    RecyclerView.Adapter<SlidderProductAdapter.SingleRow>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        return SingleRow(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.item_image_slider, p0, false
            )
        )


    }

    override fun getItemCount(): Int {
        return pathes.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemImageSliderBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val category = pathes[pos]
            AppUtils.loadGlideImage(view.root.context,category,R.drawable.default_image,view.image)
            view.root.setOnClickListener{
                val intent = Intent(view.root.context, FullScreenImageScrollActivity::class.java)
                intent.putExtra(FullScreenImageScrollActivity.KEY_CURRENT_ITEM_POSITION, pos)
                intent.putStringArrayListExtra(FullScreenImageScrollActivity.KEY_IMAGE_GALLERY_DATA, pathes)
                view.root.context.startActivity(intent)
            }
        }
    }


}