package com.muralex.worldnews.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ShareCompat
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.Constants.CONTACT_EMAIL
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


class ContactActions @Inject constructor(
    @ActivityContext private val context: Context,
) {

    fun sendFeedback() {
        sendEmailWithData(context.getString(R.string.feedback_subject), "")
    }

    fun sendReport() {
        sendEmailWithData(context.getString(R.string.error_report_subject), "")
    }

    fun rateApp() {
        sendEmailWithData(context.getString(R.string.five_star_feedback), "")
    }

    fun shareApp() {
        ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setChooserTitle(context.getString(R.string.text_share))
            .setText(context.getString(R.string.text_share_app))
            .startChooser()
    }

    fun shareURL(url: String) {
        ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setChooserTitle(context.getString(R.string.text_share))
            .setText(context.getString(R.string.share_url) + url)
            .startChooser()
    }

    private fun sendEmail() {
        sendEmailWithData("", "")
    }

    fun sendEmailWithData(subject: String, body: String) {
        val i = Intent(Intent.ACTION_SENDTO)
        val mailto = "mailto:" + CONTACT_EMAIL +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(body)
        i.data = Uri.parse(mailto)
        try {
            context.startActivity(Intent.createChooser(i, ""))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.no_mail_client), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToPlayStore(pack: String) {
        val uri = Uri.parse("market://details?id=$pack")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$pack")))
        }
    }

    fun openUrl(url: String) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            context.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.no_mail_client), Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

}