package com.digvijay.newsapp.util

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.core.text.HtmlCompat
import java.text.SimpleDateFormat
import java.util.*

fun TextView.setDate(dateString: String){
    val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val date: Date? = fmt.parse(dateString)
    val fmtOut = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    this.text = fmtOut.format(date?:"")
}

fun TextView.setHtmlText(htmlString: String?){
    if(!htmlString.isNullOrEmpty()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.text = Html.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            this.text = Html.fromHtml(htmlString)
        }
    }else{
        this.text = ""
    }
}