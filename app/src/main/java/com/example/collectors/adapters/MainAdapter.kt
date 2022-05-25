package com.example.collectors.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collectors.Category
import com.example.collectors.Categoryy

import com.example.collectors.R
import com.example.collectors.database.BasicInfo
import kotlinx.android.synthetic.main.main_list_item_view.view.*
import kotlinx.android.synthetic.main.movie_item_view.view.*


class MainAdapter(
        private val list: ArrayList<Categoryy>,
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.main_list_item_view, parent,false
            )
        )
    }
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = list[position]

        if(holder is MyViewHolder) {
            holder.itemView.tvCategory.text = item.category

            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            holder.itemView.rvCategoryList.layoutManager = linearLayoutManager

            holder.itemView.rvCategoryList.adapter = SecondAdapter(
                    item, item.list, context,
                    {it},{id, like ->{}})
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}