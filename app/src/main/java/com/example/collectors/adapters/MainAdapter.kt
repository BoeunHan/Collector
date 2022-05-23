package com.example.collectors.adapters


import android.content.Context
import android.content.Intent
import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.activities.MyMovieDetail
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.movie_item_view.view.*


class MainAdapter(
    private val list: ArrayList<String>,
    private val context: Context
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


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}