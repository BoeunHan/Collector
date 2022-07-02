package com.han.collector.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.databinding.MovieSearchItemViewBinding
import com.han.collector.model.data.networkModel.MovieItem
import com.han.collector.view.activities.SearchActivity
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MovieSearchAdapter(
    private val activity: SearchActivity
) : PagingDataAdapter<MovieItem, MovieSearchAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    inner class MovieViewHolder(
        private val binding: MovieSearchItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieItem) {
            binding.item = data
            binding.activity = activity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieSearchItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }


    companion object {
        val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.link == newItem.link
            }

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}