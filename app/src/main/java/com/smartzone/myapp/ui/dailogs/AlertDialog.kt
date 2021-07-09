package com.smartzone.myapp.ui.dailogs

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.AlertDialogBinding

class AlertDialog (context: Context) : AlertDialog(context){

    lateinit var binding: AlertDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.alert_dialog,
            null,
            false
        )
        setContentView(binding.getRoot())
        val window = window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        window.attributes.windowAnimations = R.style.PopupAnimation
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (context.resources.displayMetrics.widthPixels * .85).toInt()
        getWindow()!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp

    }

    fun show(message:String) {
        show()
        binding.tittle.text=message
        binding.doneBuy.setOnClickListener {
            dismiss()
        }

    }





}