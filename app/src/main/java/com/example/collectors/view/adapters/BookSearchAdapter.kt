package com.example.collectors.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.databinding.BookSearchItemViewBinding
import com.example.collectors.databinding.MovieSearchItemViewBinding
import com.example.collectors.model.data.networkModel.BookItem
import com.example.collectors.model.data.networkModel.MovieItem
import com.example.collectors.view.activities.SearchActivity
import kotlinx.coroutines.FlowPreview

@FlowPreview
class BookSearchAdapter(
    private val bookList: ArrayList<BookItem>,
    private val activity: SearchActivity
) : RecyclerView.Adapter<BookSearchAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        val binding: BookSearchItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            BookSearchItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.binding.item = bookList[position]
        holder.binding.activity = activity
        Log.e("bookitem","${bookList[position].title},${bookList[position].image}")
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}