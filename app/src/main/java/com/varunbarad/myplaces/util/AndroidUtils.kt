@file:JvmName("AndroidUtils")

package com.varunbarad.myplaces.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.varunbarad.myplaces.R

fun createIntentToOpenCoordinatesOnMap(latitude: Double, longitude: Double): Intent {
    return Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:${latitude},${longitude}")
    )
}

fun Context.copyStringToClipboard(textContents: String) {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(
        this.resources.getString(R.string.app_name),
        textContents
    )
    clipboardManager.setPrimaryClip(clipData)
}
