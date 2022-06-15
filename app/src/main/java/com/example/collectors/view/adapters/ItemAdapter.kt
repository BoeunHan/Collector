package com.example.collectors.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.model.data.database.BasicInfo
import com.example.collectors.databinding.CardItemViewBinding
import com.example.collectors.view.activities.ItemListActivity
import com.example.collectors.view.activities.MainActivity
import com.example.collectors.viewmodel.ItemViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.intellij.lang.annotations.Flow

@FlowPreview
class ItemAdapter(
    private val itemList: ArrayList<BasicInfo>,
    private val category: String,
    private val activity: AppCompatActivity,
    private val viewModel: ItemViewModel
) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        val binding: CardItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root){
        val isSelected = MutableStateFlow(false)
    }

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
        holder.binding.viewmodel = viewModel
        holder.binding.holder = holder

        holder.binding.lifecycleOwner = activity
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}