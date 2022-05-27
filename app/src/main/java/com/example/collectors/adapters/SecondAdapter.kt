package com.example.collectors.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.collectors.R
import com.example.collectors.database.BasicInfo
import kotlinx.android.synthetic.main.card_item_view.view.*


class SecondAdapter(
        private val list: ArrayList<BasicInfo>,
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
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
            holder.itemView.myRatingBar.visibility = View.GONE
            if(item.like) holder.itemView.ivLike.visibility = View.VISIBLE
            else holder.itemView.ivLike.visibility = View.GONE
            if(item.image!="")  Glide.with(holder.itemView).load(item.image).into(holder.itemView.ivItemImage)
            else {
                holder.itemView.ivItemImage.scaleType = ImageView.ScaleType.CENTER
                Glide.with(holder.itemView).load(R.drawable.ic_no_image).into(holder.itemView.ivItemImage)
            }

            holder.itemView.tvTitleItem.text = item.title

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}