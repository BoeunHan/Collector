package com.han.collector.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.databinding.BookSearchItemViewBinding
import com.han.collector.model.data.remote.model.BookItem
import com.han.collector.view.activities.SearchActivity
import kotlinx.coroutines.FlowPreview

@FlowPreview
class BookSearchAdapter constructor(
    val activity: SearchActivity
) : PagingDataAdapter<BookItem, BookSearchAdapter.BookViewHolder>(BOOK_COMPARATOR) {

    inner class BookViewHolder(
        private val binding: BookSearchItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BookItem) {
            binding.item = data
            binding.activity = activity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookSearchItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }


    companion object {
        val BOOK_COMPARATOR = object : DiffUtil.ItemCallback<BookItem>() {
            override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
                return oldItem.link == newItem.link
            }

            override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}