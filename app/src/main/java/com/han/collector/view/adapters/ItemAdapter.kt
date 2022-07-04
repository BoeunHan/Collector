package com.han.collector.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.databinding.CardItemViewBinding
import com.han.collector.utils.Constants
import com.han.collector.view.activities.ItemListActivity
import com.han.collector.view.activities.MainActivity
import com.han.collector.viewmodel.ItemViewModel
import kotlinx.coroutines.FlowPreview

@FlowPreview
class ItemAdapter(
    private val category: String,
    private val activity: AppCompatActivity,
    private val viewModel: ItemViewModel
) : PagingDataAdapter<BasicInfo, ItemAdapter.ReviewViewHolder>(Constants.REVIEW_COMPARATOR) {

    inner class ReviewViewHolder(
        private val binding: CardItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BasicInfo) {
            binding.item = data
            binding.isMain = activity is MainActivity
            if(activity is MainActivity) binding.mainActivity = activity
            if(activity is ItemListActivity) binding.itemListActivity = activity
            binding.category = category
            binding.viewModel = viewModel

            binding.lifecycleOwner = activity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = CardItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }
}