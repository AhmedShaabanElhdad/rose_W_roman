package com.smartzone.horizon.ui.main.home



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ItemMainCategoryViewBinding
import com.smartzone.myapp.data.pojo.Category
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.utilis.AppUtils
import com.smartzone.myapp.utilis.LanguageType
import com.smartzone.myapp.utilis.SavePrefs

class CategoryAdapter(
    val categories: List<Category>,
    val click: (Category) -> Unit

) :
    RecyclerView.Adapter<CategoryAdapter.SingleRow>() {

    lateinit var pref: SavePrefs<User>

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CategoryAdapter.SingleRow {
        pref = SavePrefs(p0.context,User::class.java)
        return SingleRow(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.item_main_category_view, p0, false
            )
        )


    }

    override fun getItemCount(): Int {
        return categories.size
    }


    override fun onBindViewHolder(holder: CategoryAdapter.SingleRow, p1: Int) {
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemMainCategoryViewBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(pos: Int) {
            val category = categories[pos]
            if (pref.getLanguage() == LanguageType.ARABIC.lang)
                view.tittle.text=category.name_ar
            else
                view.tittle.text=category.name
            AppUtils.loadGlideImage(view.root.context,category.image,R.drawable.default_image,view.categoryImage)
            view.root.setOnClickListener {
                click(category)
            }

        }
    }


}