package com.han.collector.view.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.databinding.MainListItemViewBinding
import com.han.collector.view.activities.MainActivity
import kotlinx.coroutines.FlowPreview

@FlowPreview
class CategoryAdapter(
    private val categoryList: List<String>,
    private val activity: MainActivity,
    private val setCategoryList: (String, RecyclerView)->Unit
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(){

    inner class MyViewHolder(
        val binding: MainListItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root){
        val rvCategoryList = binding.rvCategoryList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MainListItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.activity = activity
        holder.binding.category = categoryList[position]

        setCategoryList(categoryList[position], holder.rvCategoryList)

    }

    override fun getItemCount(): Int = categoryList.size
}