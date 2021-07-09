package com.smartzone.myapp.ui.main.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ItemCategoryBinding
import com.smartzone.diva_wear.databinding.ItemCategoryViewBinding
import com.smartzone.myapp.utilis.LanguageType
import com.smartzone.myapp.utilis.SavePrefs
import com.smartzone.myapp.data.pojo.Category
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.utilis.AppUtils


class CategoryAdapter(
    val categories: List<Category>,
    val viewType: Int = VIEW_TEXT,
    val click: (Category) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_IMAGE = 1
        const val VIEW_TEXT = 2
    }


    lateinit var pref: SavePrefs<User>
    var selectedItemPos = 0
    var lastItemSelectedPos = 0

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        pref = SavePrefs(p0.context, User::class.java)
        return if (viewType == VIEW_TEXT)
            SingleRow(
                DataBindingUtil.inflate(
                    LayoutInflater.from(p0.context),
                    R.layout.item_category, p0, false
                )
            )
        else
            SingleRowWithImage(
                DataBindingUtil.inflate(
                    LayoutInflater.from(p0.context),
                    R.layout.item_category_view, p0, false
                )
            )



    }

    override fun getItemCount(): Int {
        return categories.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (viewType === VIEW_IMAGE) {
            (holder as SingleRowWithImage).bind(position)
        } else {
            (holder as SingleRow).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }


    private inner class SingleRow(var view: ItemCategoryBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val category = categories[pos]

            if (pos == selectedItemPos) {
                view.txtCat.setTextColor(Color.WHITE)
                view.cardCat.setCardBackgroundColor(view.root.context.resources.getColor(R.color.red))
            } else {
                view.txtCat.setTextColor(Color.DKGRAY)
                view.cardCat.setCardBackgroundColor(view.root.context.resources.getColor(R.color.white))
            }

            if (pref.getLanguage().equals(LanguageType.ARABIC.lang))
                view.txtCat.text = category.name_ar
            else
                view.txtCat.text = category.name

            view.root.setOnClickListener {
                selectedItemPos = adapterPosition
                lastItemSelectedPos = if (lastItemSelectedPos == -1)
                    selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    selectedItemPos
                }
                notifyItemChanged(selectedItemPos)

                click(category)
            }

        }
    }


    private inner class SingleRowWithImage(var view: ItemCategoryViewBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val category = categories[pos]


            if (pref.getLanguage().equals(LanguageType.ARABIC.lang))
                view.tittle.text = category.name_ar
            else
                view.tittle.text = category.name


            category.image?.let {
                AppUtils.loadGlideImage(
                    view.root.context,
                    it,
                    R.drawable.default_image,
                    view.categoryImage
                )
            }

            view.root.setOnClickListener {
                click(category)
            }

        }
    }


}