package com.smartzone.myapp.ui.dailogs

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.City
import com.smartzone.diva_wear.databinding.LayoutCityDialogBinding
import com.smartzone.myapp.ui.order_details.DeliveryCityAdapter
import com.smartzone.myapp.ui.register.CityAdapter

class CityDialog (context: Context) : AlertDialog(context){

    lateinit var binding: LayoutCityDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_city_dialog,
            null,
            false
        )
        setContentView(binding.getRoot())
        val window = window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        binding.rv.layoutManager=LinearLayoutManager(context)
        window.attributes.windowAnimations = R.style.PopupAnimation
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val height = (context.resources.displayMetrics.heightPixels * .65).toInt()
        getWindow()!!.setLayout( WindowManager.LayoutParams.MATCH_PARENT,height)
        window.setGravity(Gravity.BOTTOM)
        val wlp = window.attributes
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp

    }

    fun show(list: List<City>, isHowTittle: Boolean, click:(City)->Unit) {
        show()
        if (!isHowTittle){
            binding.rv.adapter= CityAdapter(list){
                dismiss()
                click(it)
            }
        }else{
            binding.rv.adapter= DeliveryCityAdapter(list){
                dismiss()
                click(it)
            }
        }

    }
}