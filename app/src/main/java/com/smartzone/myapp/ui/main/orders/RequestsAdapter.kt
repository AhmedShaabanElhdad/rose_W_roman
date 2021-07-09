package com.smartzone.myapp.ui.main.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.data.reponse.Request
import com.smartzone.diva_wear.databinding.ItemOrderBinding
import com.smartzone.myapp.utilis.SavePrefs

class RequestsAdapter(
    val list: List<Request>
    , val clicReques: (Request) -> Unit
) :
    RecyclerView.Adapter<RequestsAdapter.SingleRow>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): SingleRow {
        return SingleRow(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.item_order, p0, false
            )
        )


    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: SingleRow, p1: Int) {
        holder.mBinding!!.pref = SavePrefs(holder.itemView.context, User::class.java)
        holder.bind(p1)
    }


    inner class SingleRow(var view: ItemOrderBinding) :
        RecyclerView.ViewHolder(view.root) {

        val mBinding: ItemOrderBinding? = DataBindingUtil.bind(itemView)

        fun bind(pos: Int) {

            val request = list[pos]
            view.valueOrderNum.text = request.id
            view.tvPrice.text = "${request.price} ${view.root.context.getString(R.string.currency)}"
            view.tvDate.text = request.created
//            request.Product?.let {
//                AppUtils.loadGlideImage(
//                    view.root.context,
//                    it.image,
//                    R.drawable.default_image,
//                    view.imageProduct
//                )
//            }
            view.root.setOnClickListener {
                clicReques(request)
            }
        }
    }


}