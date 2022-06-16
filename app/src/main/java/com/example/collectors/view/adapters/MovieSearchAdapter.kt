package com.example.collectors.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.databinding.MovieSearchItemViewBinding
import com.example.collectors.model.data.networkModel.MovieItem
import com.example.collectors.view.activities.SearchActivity
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MovieSearchAdapter(
    private val movieList: ArrayList<MovieItem>,
    private val activity: SearchActivity
) : RecyclerView.Adapter<MovieSearchAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        val binding: MovieSearchItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            MovieSearchItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.binding.item = movieList[position]
        holder.binding.activity = activity
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}