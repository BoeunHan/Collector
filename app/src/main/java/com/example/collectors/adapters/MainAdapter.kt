package com.example.collectors.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.collectors.R
import com.example.collectors.activities.ItemListActivity
import com.example.collectors.activities.MovieSearchActivity
import kotlinx.android.synthetic.main.main_list_item_view.view.*


class MainAdapter(
        private val list: ArrayList<String>,
        private val context: Context,
        private val setCategoryList: (String, RecyclerView)->Unit
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
            holder.itemView.tvCategory.text = item

            setCategoryList(item, holder.itemView.rvCategoryList)

            holder.itemView.btAdd.setOnClickListener {
                var intent: Intent? = null
                when(item){
                    "MOVIE" -> { intent = Intent(context, MovieSearchActivity::class.java) }
                    "BOOK" -> { intent = Intent(context, MovieSearchActivity::class.java) }
                }
                intent?.putExtra("Category", item)
                context.startActivity(intent)
            }
            holder.itemView.btMore.setOnClickListener {
                val intent = Intent(context, ItemListActivity::class.java)
                intent.putExtra("Category", item)
                context.startActivity(intent)
            }



        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}