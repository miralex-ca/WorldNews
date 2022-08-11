package com.muralex.worldnews.presentation.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.muralex.worldnews.R

@BindingAdapter("setListItemImage")
fun ImageView.setListItemImage(imageUrl: String?) {

    if (imageUrl.isNullOrEmpty()) visibility = View.GONE
    else {
        visibility = View.VISIBLE
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(12))

        Glide.with(context)
            .load(imageUrl)
            .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.colorImagePlaceholder)))
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(requestOptions)
            .into(this)
    }
}

@BindingAdapter("setArticleSource")
fun ImageView.setArticleSource(imageUrl: String?) {

    if (imageUrl.isNullOrEmpty()) visibility = View.GONE
    else {
        visibility = View.VISIBLE
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(12))

        Glide.with(context)
            .load( imageUrl)
            .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.colorImagePlaceholder)))
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(requestOptions)
            .into(this)
    }
}
