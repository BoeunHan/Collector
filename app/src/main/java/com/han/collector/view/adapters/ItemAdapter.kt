package com.han.collector.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.databinding.CardItemViewBinding
import com.han.collector.view.activities.ItemListActivity
import com.han.collector.view.activities.MainActivity
import com.han.collector.viewmodel.ItemViewModel
import kotlinx.coroutines.FlowPreview

@FlowPreview
class ItemAdapter(
    private val itemList: ArrayList<BasicInfo>,
    private val category: String,
    private val activity: AppCompatActivity,
    private val viewModel: ItemViewModel
) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        val binding: CardItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CardItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.binding.item = itemList[position]

        holder.binding.isMain = activity is MainActivity
        if(activity is MainActivity) holder.binding.mainActivity = activity
        if(activity is ItemListActivity) holder.binding.itemListActivity = activity
        holder.binding.category = category
        holder.binding.viewModel = viewModel

        holder.binding.lifecycleOwner = activity
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}