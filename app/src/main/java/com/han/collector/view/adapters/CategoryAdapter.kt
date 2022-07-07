package com.han.collector.view.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.databinding.MainListItemViewBinding
import com.han.collector.view.activities.MainActivity
import com.han.collector.viewmodel.ItemViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@FlowPreview
class CategoryAdapter(
    private val categoryList: List<String>,
    private val activity: MainActivity,
    private val viewModel: ItemViewModel
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(){

    inner class MyViewHolder(
        val binding: MainListItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: String){
            binding.category = category
            binding.activity = activity

            setCategoryList(category, binding.rvCategoryList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MainListItemViewBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categoryList.size

    private fun setCategoryList(category: String, recyclerview: RecyclerView) {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerview.layoutManager = layoutManager
        val pagingAdapter = ItemAdapter(
            category, activity, viewModel)
        recyclerview.adapter = pagingAdapter
        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (category) {
                    "영화" -> viewModel.movieList
                    "책" -> viewModel.bookList
                    else -> flow {}
                }.collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
        }
    }
}