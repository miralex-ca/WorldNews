package com.muralex.worldnews.data.model.app

import android.accounts.AuthenticatorDescription
import java.io.Serializable

data class Article(
    val title: String,
    val description: String,
    val text: String,
    val url: String,
    val image: String
) : Serializable
