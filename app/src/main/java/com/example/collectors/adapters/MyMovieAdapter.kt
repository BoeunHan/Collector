package com.example.collectors.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.activities.MyMovieDetail
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.movie_item_view.view.*

class MyMovieAdapter(private val myMovieList: ArrayList<MovieEntity>, private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.movie_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val movie = myMovieList[position]
        if(holder is MyViewHolder) {
            holder.itemView.tvMyMovieTitle.text = movie.title
            Glide.with(context).load(movie.image).into(holder.itemView.ivMyMovieImage)
            holder.itemView.myRatingBar.rating = movie.rate
            holder.itemView.setOnClickListener {
                val intent = Intent(context, MyMovieDetail::class.java)
                intent.putExtra(Constants.SELECTED_MOVIE, movie)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return myMovieList.size
    }
}