package com.smartzone.myapp.utilis

import android.app.TimePickerDialog
import android.content.Context
import android.content.res.Resources
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object ViewUtils {

//    fun changeIconDrawableToGray(context: Context, drawable: Drawable?) {
//        if (drawable != null) {
//            drawable.mutate()
//            drawable.setColorFilter(ContextCompat.getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP)
//        }
//    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }


    fun makeSpaceBetweenTabs(tablayout: TabLayout){
        val tabs = tablayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabs.childCount ) {
            val tab = tabs.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 0f
            layoutParams.marginEnd = 6
            layoutParams.marginStart = 6
            tab.layoutParams = layoutParams
            tablayout.requestLayout()
        }
    }

     fun showDialogTime(context: Context,selectionTime:String? ,timeSelected:TimePickerDialog.OnTimeSetListener):TimePickerDialog {
         var calendar:Calendar = Calendar.getInstance()
         if (selectionTime!=null){
          val date=SimpleDateFormat("HH:mm:ss").parse(selectionTime)
             calendar.time=date!!
         }
        val dialog = TimePickerDialog(
            context,
            timeSelected,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            false
        )
        return dialog
    }

}