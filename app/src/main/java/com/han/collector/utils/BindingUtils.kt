package com.han.collector.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.han.collector.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("image")
fun loadImage(view: ImageView, image: String?) {
    if (image == "" || image == null) Glide.with(view).load(R.drawable.ic_no_image).into(view)
    else if(image.startsWith("http")) Glide.with(view).load(image).centerCrop().error(R.drawable.ic_no_image).into(view)
    else {
        val uri = Uri.parse(image)
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        view.setImageURI(uri)
    }
}
@BindingAdapter("image")
fun loadImageBitmap(view: ImageView, image: Bitmap?) {
    if (image == null) {
        view.scaleType = ImageView.ScaleType.CENTER
        Glide.with(view).load(R.drawable.ic_no_image).into(view)
    }
    else {
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        view.setImageBitmap(image)
    }
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
