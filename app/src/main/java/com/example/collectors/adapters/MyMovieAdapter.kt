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
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.movie_item_view.view.*


class MyMovieAdapter(
    private val myMovieList: ArrayList<MovieEntity>, private val context: Context,
    private val removeListener: (movie: MovieEntity) -> Unit,
    private val likeListener: (id: Int, like: Boolean) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

            if(Constants.isRemoveMode) holder.itemView.btRemoveCard.visibility = View.VISIBLE
            else holder.itemView.btRemoveCard.visibility = View.INVISIBLE

            holder.itemView.tvMyMovieTitle.text = movie.title
            Glide.with(context).load(movie.image).into(holder.itemView.ivMyMovieImage)
            holder.itemView.myRatingBar.rating = movie.rate

            if(movie.like) holder.itemView.btLike.setImageResource(R.drawable.ic_like)
            else holder.itemView.btLike.setImageResource(R.drawable.ic_dislike)

            holder.itemView.btLike.setOnClickListener {
                if(movie.like) likeListener(movie.id, false)
                else likeListener(movie.id, true)
                notifyItemChanged(position)
            }

            holder.itemView.btRemoveCard?.setOnClickListener {
                removeListener(movie)
            }

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