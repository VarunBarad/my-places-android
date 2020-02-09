@file:JvmName("AndroidUtils")

package com.varunbarad.myplaces.util

import android.content.Intent
import android.net.Uri

fun createIntentToOpenCoordinatesOnMap(latitude: Double, longitude: Double): Intent {
    return Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:${latitude},${longitude}")
    )
}
