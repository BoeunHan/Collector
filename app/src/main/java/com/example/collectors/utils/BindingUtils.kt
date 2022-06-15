package com.example.collectors.utils

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collectors.R
import com.example.collectors.model.data.database.BasicInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@BindingAdapter("image")
fun loadImage(view: ImageView, url: String){
    if(url!="")  Glide.with(view).load(url).into(view)
    else {
        view.scaleType = ImageView.ScaleType.CENTER
        Glide.with(view).load(R.drawable.ic_no_image).into(view)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("selected")
fun loadDrawable(view: ImageView, selected: Boolean){
    if(selected) view.foreground = ContextCompat.getDrawable(view.context, R.drawable.gradient_shape_selected)
    else view.foreground = ContextCompat.getDrawable(view.context, R.drawable.gradient_shape)
}


@BindingAdapter("text")
fun setText(view: TextView, str: String){
    when(view.id){
        R.id.tvTitle -> {
            view.text = Html.fromHtml(str).toString()
        }
        R.id.tvDirector -> {
            if (str != "") {
                view.text = "감독: ${str.substring(0, str.length - 1)}"
            }
        }
        R.id.tvActor -> {
            if (str != "") {
                view.text = "출연: ${str.substring(0, str.length - 1)}"
            }
        }
    }
}
