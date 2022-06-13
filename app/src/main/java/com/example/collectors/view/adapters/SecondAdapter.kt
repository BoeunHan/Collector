package com.example.collectors.view.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.model.data.database.BasicInfo
import com.example.collectors.databinding.CardItemViewBinding
import com.example.collectors.view.activities.MainActivity

class SecondAdapter(
    private val itemList: ArrayList<BasicInfo>,
    private val category: String,
    private val activity: MainActivity
) : RecyclerView.Adapter<SecondAdapter.MyViewHolder>() {

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
        holder.binding.ratingVisibility = false
        holder.binding.category = category
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}