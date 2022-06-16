package com.example.collectors.utils

import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.collectors.R

@BindingAdapter("image")
fun loadImage(view: ImageView, url: String?){
    if(url==""|| url==null){
        Glide.with(view).load(R.drawable.ic_no_image).into(view)
    }
    else  Glide.with(view).load(url).centerCrop().error(R.drawable.ic_no_image).into(view)
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
        R.id.tvDirector -> {
            if (str != "") view.text = "감독: ${str.substring(0, str.length - 1)}"
        }
        R.id.tvActor -> {
            if (str != "") view.text = "출연: ${str.substring(0, str.length - 1)}"
        }
        R.id.tvAuthor -> {
            if (str != "") view.text = "저자: $str"
        }
        R.id.tvPublisher -> {
            if (str != "") view.text = "출판사: $str"
        }
    }
}
