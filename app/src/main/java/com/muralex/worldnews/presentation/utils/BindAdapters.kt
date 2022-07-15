package com.muralex.worldnews.presentation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("setListItemImage")
fun ImageView.setListItemImage(imageUrl: String?) {
    imageUrl?.let {

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(10))

        Glide.with(context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(requestOptions)
            .into(this)
    }
}

@BindingAdapter("setArticleSource")
fun ImageView.setArticleSource(imageUrl: String?) {
    imageUrl?.let {

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(10))

        Glide.with(context)
            .load( imageUrl)
            .apply(requestOptions)
            .into(this)
    }
}
