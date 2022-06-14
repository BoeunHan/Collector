package com.example.collectors.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.model.data.database.BasicInfo
import com.example.collectors.databinding.CardItemViewBinding
import com.example.collectors.viewmodel.ItemViewModel
import kotlinx.coroutines.FlowPreview

@FlowPreview
class ItemAdapter(
    private val list: ArrayList<BasicInfo>
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
        holder.binding.item = list[position]
        holder.binding.ratingVisibility = true

    }

    override fun getItemCount(): Int {
        return list.size
    }
}