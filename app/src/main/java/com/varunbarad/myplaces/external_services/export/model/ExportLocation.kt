package com.varunbarad.myplaces.external_services.export.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ExportLocation(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "comments") val comments: String,
    @Json(name = "latitude") val latitude: String,
    @Json(name = "longitude") val longitude: String,
    @Json(name = "timestamp") val timestamp: Date,
)
