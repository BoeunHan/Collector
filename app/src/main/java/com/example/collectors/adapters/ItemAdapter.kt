package com.example.collectors.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.activities.MovieDetailActivity
import com.example.collectors.database.BasicInfo
import kotlinx.android.synthetic.main.card_item_view.view.*


class ItemAdapter(
        private val category: String,
        private val list: ArrayList<BasicInfo>,
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val selectedItem = HashSet<Int>()
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_item_view, parent,false
            )
        )
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = list[position]

        if(holder is MyViewHolder) {
            if(item.image!="")  Glide.with(holder.itemView).load(item.image).into(holder.itemView.ivItemImage)
            else {
                holder.itemView.ivItemImage.scaleType = ImageView.ScaleType.CENTER
                Glide.with(holder.itemView).load(R.drawable.ic_no_image).into(holder.itemView.ivItemImage)
            }
            holder.itemView.tvTitleItem.text = item.title
            holder.itemView.myRatingBar.rating = item.rate
            if(item.like) holder.itemView.ivLike.visibility = View.VISIBLE
            else holder.itemView.ivLike.visibility = View.GONE

            if(Constants.selectMode){
                if(selectedItem.contains(item.id)){
                    holder.itemView.ivChecked.visibility = View.VISIBLE
                    holder.itemView.ivItemImage.foreground = ContextCompat.getDrawable(context, R.drawable.gradient_shape_selected)
                }else{
                    holder.itemView.ivChecked.visibility = View.GONE
                    holder.itemView.ivItemImage.foreground = ContextCompat.getDrawable(context, R.drawable.gradient_shape)
                }
            }
            holder.itemView.setOnClickListener {
                if(Constants.selectMode){
                    if(selectedItem.contains(item.id)){
                        holder.itemView.ivChecked.visibility = View.GONE
                        holder.itemView.ivItemImage.foreground = ContextCompat.getDrawable(context, R.drawable.gradient_shape)
                        selectedItem.remove(item.id)
                    }else{
                        holder.itemView.ivChecked.visibility = View.VISIBLE
                        holder.itemView.ivItemImage.foreground = ContextCompat.getDrawable(context, R.drawable.gradient_shape_selected)
                        selectedItem.add(item.id)
                    }
                    changeFabStatus()
                }else {
                    when (category) {
                        "MOVIE" -> {
                            val intent = Intent(context, MovieDetailActivity::class.java)
                            intent.putExtra(Constants.ID, item.id)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private lateinit var fabListener: (b: Boolean) -> Unit

    fun setFabListener(listener: (b: Boolean)-> Unit){
        fabListener = listener
    }

    private fun changeFabStatus(){
        if(selectedItem.isEmpty()) fabListener(false)
        else fabListener(true)
    }

    fun getSelectedItem(): HashSet<Int> {
        return selectedItem
    }
}