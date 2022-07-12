package com.han.collector.utils

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.han.collector.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("image")
fun loadImage(view: ImageView, url: String?) {
    if (url == "" || url == null) Glide.with(view).load(R.drawable.ic_no_image).into(view)
    else Glide.with(view).load(url).centerCrop().error(R.drawable.ic_no_image).into(view)
}

@BindingAdapter("circleImage")
fun loadImage(view: CircleImageView, url: String?){
    if (url == "" || url == null) Glide.with(view).load(R.drawable.ic_user).into(view)
    else Glide.with(view).load(url).error(R.drawable.ic_user).into(view)
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("selected")
fun loadDrawable(view: ImageView, selected: Boolean) {
    if (selected) view.foreground =
        ContextCompat.getDrawable(view.context, R.drawable.gradient_shape_selected)
    else view.foreground = ContextCompat.getDrawable(view.context, R.drawable.gradient_shape)
}

@BindingAdapter("scrollListener")
fun setOnScrollListener(view: RecyclerView, listener: RecyclerView.OnScrollListener){
    view.addOnScrollListener(listener)
}
