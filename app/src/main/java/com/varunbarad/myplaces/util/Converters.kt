@file:JvmName("Converters")

package com.varunbarad.myplaces.util

import com.varunbarad.myplaces.external_services.export.model.ExportLocation
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import com.varunbarad.myplaces.model.UiLocation

fun DbLocation.toUiLocation(): UiLocation {
    return UiLocation(
        locationId = this.id
            ?: throw IllegalArgumentException("Location to be shown has to have an ID"),
        name = this.name,
        comments = this.comments,
        timestamp = this.timestamp,
        latitude = this.latitude.toDouble(),
        longitude = this.longitude.toDouble()
    )
}

fun UiLocation.toDbLocation(): DbLocation {
    return DbLocation(
        id = this.locationId,
        name = this.name,
        comments = this.comments,
        timestamp = this.timestamp,
        latitude = this.latitude.toString(),
        longitude = this.longitude.toString()
    )
}

fun DbLocation.toExportLocation(): ExportLocation {
    return ExportLocation(
        id = this.id
            ?: throw IllegalArgumentException("Location to be exported has to have an ID"),
        name = this.name,
        comments = this.comments,
        latitude = this.latitude,
        longitude = this.longitude,
        timestamp = this.timestamp
    )
}
