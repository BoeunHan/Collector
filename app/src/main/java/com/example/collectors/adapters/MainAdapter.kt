package com.example.collectors.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.collectors.R
import com.example.collectors.activities.MovieSearchActivity
import com.example.collectors.database.BasicInfo
import com.example.collectors.database.MovieEntity

import kotlinx.android.synthetic.main.main_list_item_view.view.*



class MainAdapter(
    private val list: ArrayList<Pair<String, ArrayList<BasicInfo>>>,
    private val context: Context,
    private val removeListener: (id: Int) -> Unit,
    private val likeListener: (id: Int, like: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.main_list_item_view,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val category = list[position]
        if(holder is MyViewHolder) {
            holder.itemView.tvCategory.text = category.first

            holder.itemView.btAdd.setOnClickListener {
                when(category.first) {
                    "영화" -> {
                        val intent = Intent(context, MovieSearchActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
            holder.itemView.btMore.setOnClickListener {
                when(category.first) {
                    "영화" -> {
                    }
                }
            }


            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            holder.itemView.rvCategoryList.layoutManager = linearLayoutManager
            holder.itemView.rvCategoryList.adapter = CategoryAdapter(
                category.second,
                context,
                removeListener,
                likeListener
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}