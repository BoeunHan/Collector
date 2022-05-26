package com.example.collectors.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.activities.AddMovieActivity
import com.example.collectors.models.Item
import kotlinx.android.synthetic.main.movie_search_item_view.view.*

class MovieSearchAdapter(private val movieList: ArrayList<Item>, private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.movie_search_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val movie = movieList[position]
        if(holder is MyViewHolder) {
            if(movie.image!="")  Glide.with(holder.itemView).load(movie.image).into(holder.itemView.ivImage)
            else {
                holder.itemView.ivImage.scaleType = ImageView.ScaleType.CENTER
                Glide.with(holder.itemView).load(R.drawable.ic_no_image).into(holder.itemView.ivImage)
            }
            holder.itemView.tvTitle.text = Html.fromHtml(movie.title).toString()
            holder.itemView.tvPubDate.text = movie.pubDate
            if(movie.director!=""){
                val str = movie.director
                holder.itemView.tvDirector.text = "감독: ${str.substring(0, str.length - 1)}"
            }
            if(movie.actor!=""){
                val str = movie.actor
                holder.itemView.tvActor.text = "출연: ${str.substring(0, str.length - 1)}"
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, AddMovieActivity::class.java)
                intent.putExtra(Constants.MOVIE_IMAGE, movie.image)
                intent.putExtra(Constants.MOVIE_TITLE, movie.title)
                context.startActivity(intent)
                (context as Activity).finish()
            }

        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }


}