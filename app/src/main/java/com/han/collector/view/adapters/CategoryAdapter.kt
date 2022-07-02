package com.han.collector.view.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.databinding.MainListItemViewBinding
import com.han.collector.model.data.database.BasicInfo
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
        activity.lifecycleScope.launch {
            when (category) {
                "영화" -> viewModel.movieList
                "책" -> viewModel.bookList
                else -> flow {}
            }.collectLatest {
                val arrayList = ArrayList(it)
                setAdapter(arrayList, recyclerview, category)
            }
        }
    }

    private fun setAdapter(
        list: ArrayList<BasicInfo>,
        recyclerview: RecyclerView,
        category: String
    ) {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerview.layoutManager = layoutManager

        recyclerview.adapter = ItemAdapter(
            list, category, activity, viewModel
        )
    }

}