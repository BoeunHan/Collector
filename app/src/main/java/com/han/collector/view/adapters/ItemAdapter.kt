package com.han.collector.view.adapters

import android.util.Log
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
import kotlinx.coroutines.FlowPreview

@FlowPreview
class ItemAdapter(
    private val _category: String,
    private val _activity: AppCompatActivity
) : PagingDataAdapter<BasicInfo, ItemAdapter.ReviewViewHolder>(Constants.REVIEW_COMPARATOR) {

    inner class ReviewViewHolder(
        private val binding: CardItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BasicInfo) {
            binding.apply {
                item = data
                isMain = _activity is MainActivity
                if(_activity is MainActivity) {
                    mainActivity = _activity
                    viewModel = _activity.viewModel
                }
                if (_activity is ItemListActivity) {
                    itemListActivity = _activity
                    viewModel = _activity.viewModel
                }
                category = _category
                lifecycleOwner = _activity
            }
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