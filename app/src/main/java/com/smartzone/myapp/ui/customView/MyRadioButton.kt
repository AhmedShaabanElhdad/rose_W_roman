package com.smartzone.myapp.ui.customView


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.Address

class MyRadioButton : AppCompatRadioButton {
    private var view: View? = null
    private var txtName: TextView? = null
    private var txtAddress: TextView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    // setText is a final method in ancestor, so we must take another name.
    fun setAddressNameWith(resId: Int) {
        txtName!!.setText(resId)
        redrawLayout()
    }

    private var address:Address? = null
    fun saveAddress(address:Address){
        this.address = address
    }

    fun getAddress(): Address? {
        return address
    }
    fun setAddressNameWith(text: CharSequence?) {
        txtName!!.text = text
        redrawLayout()
    }    // setText is a final method in ancestor, so we must take another name.
    fun setAddressDetailsWith(resId: Int) {
        txtAddress!!.setText(resId)
        redrawLayout()
    }

    fun setAddressDetailsWith(text: CharSequence?) {
        txtAddress!!.text = text
        redrawLayout()
    }

    private fun init(context: Context) {
        view = LayoutInflater.from(context).inflate(R.layout.my_radio_button_content, null)
        txtName = view!!.findViewById(R.id.txt_address_name)
        txtAddress = view!!.findViewById(R.id.txt_address_details)
        redrawLayout()
    }

    private fun redrawLayout() {
        view!!.isDrawingCacheEnabled = true
        view!!.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        view!!.layout(0, 0, view!!.measuredWidth, view!!.measuredHeight)
        view!!.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(view!!.drawingCache)
        setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(resources, bitmap), null, null, null)
        view!!.isDrawingCacheEnabled = false
    }

    private fun dp2px(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}