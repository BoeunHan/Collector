package com.han.collector.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.databinding.MovieSearchItemViewBinding
import com.han.collector.databinding.PlaceSearchItemViewBinding
import com.han.collector.model.data.remote.model.MovieItem
import com.han.collector.model.data.remote.model.PlaceItem
import com.han.collector.view.activities.SearchActivity
import kotlinx.coroutines.FlowPreview

@FlowPreview
class PlaceSearchAdapter(
    private val activity: SearchActivity
) : PagingDataAdapter<PlaceItem, PlaceSearchAdapter.PlaceViewHolder>(PLACE_COMPARATOR) {

    inner class PlaceViewHolder(
        private val binding: PlaceSearchItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PlaceItem) {
            binding.item = data
            binding.activity = activity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = PlaceSearchItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }


    companion object {
        val PLACE_COMPARATOR = object : DiffUtil.ItemCallback<PlaceItem>() {
            override fun areItemsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
                return oldItem.mapx == newItem.mapx && oldItem.mapy == newItem.mapy
            }

            override fun areContentsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}