package com.han.collector.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


interface BitmapCallback{
    fun doWithBitmap(bitmap: Bitmap?)
}
object FunctionUtils{
    fun loadBitmapFromUrl(context: Context, imageUrl: String?, callback: BitmapCallback) {
        Glide.with(context).asBitmap()
            .load(imageUrl)
            .override(300)
            .fitCenter()
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback.doWithBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) { }
            })
    }
}
