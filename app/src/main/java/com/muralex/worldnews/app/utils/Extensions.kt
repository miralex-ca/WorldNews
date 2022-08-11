package com.muralex.worldnews.app.utils

import android.util.Log
import android.view.View
import java.text.DateFormat
import java.util.*


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.displayIf(visible: Boolean) {
    visibility = if (visible) View.VISIBLE
    else View.GONE
}


fun Long.formatToShortDate(locale: Locale = Locale.US): String {
    var time = ""
    try {
        val df: DateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
        time = df.format(this)
    } catch (e: Exception) {
        Log.d("MyTag", "Error: ${e.message}")
    }
    return time
}

fun Long.formatToDate(locale: Locale = Locale.US): String {
    var time = ""
    try {
        val df: DateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale)
        time = df.format(this)
    } catch (e: Exception) {
        Log.d("MyTag", "Error: ${e.message}")
    }
    return time
}







