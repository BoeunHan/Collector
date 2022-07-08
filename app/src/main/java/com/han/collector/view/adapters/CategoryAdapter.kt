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
    private val _activity: MainActivity
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(){

    inner class MyViewHolder(
        val binding: MainListItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(_category: String){
            binding.apply {
                category = _category
                activity = _activity
                setCategoryList(_category, rvCategoryList)
            }
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
        val layoutManager = LinearLayoutManager(_activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerview.layoutManager = layoutManager
        val pagingAdapter = ItemAdapter(category, _activity)
        recyclerview.adapter = pagingAdapter
        _activity.getCategoryList(category, pagingAdapter)
    }
}