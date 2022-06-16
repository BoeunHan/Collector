package com.example.collectors.view.adapters


import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.model.data.database.BasicInfo
import com.example.collectors.databinding.CardItemViewBinding
import com.example.collectors.view.activities.MainActivity
import kotlinx.coroutines.FlowPreview

@FlowPreview
class SecondAdapter(
    private val itemList: ArrayList<BasicInfo>,
    private val category: String,
    private val activity: AppCompatActivity
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
        holder.binding.isMain = activity is MainActivity
        holder.binding.category = category
        //holder.binding.mainActivity = activity
        holder.binding.lifecycleOwner = activity
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}