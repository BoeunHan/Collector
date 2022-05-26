package com.example.collectors.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.activities.MyMovieDetail
import com.example.collectors.database.BasicInfo
import kotlinx.android.synthetic.main.card_item_view.view.*


class ItemAdapter(
        private val category: String,
        private val list: ArrayList<BasicInfo>,
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_item_view, parent,false
            )
        )
    }
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = list[position]

        if(holder is MyViewHolder) {
            if(item.image!="")  Glide.with(holder.itemView).load(item.image).into(holder.itemView.ivImageItem)
            else {
                holder.itemView.ivImageItem.scaleType = ImageView.ScaleType.CENTER
                Glide.with(holder.itemView).load(R.drawable.ic_no_image).into(holder.itemView.ivImageItem)
            }
            holder.itemView.tvTitleItem.text = item.title
            holder.itemView.myRatingBar.rating = item.rate

            holder.itemView.setOnClickListener {
                when(category) {
                    "MOVIE" -> {
                        val intent = Intent(context, MyMovieDetail::class.java)
                        intent.putExtra(Constants.ID, item.id)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}