package com.varunbarad.myplaces.external_services.export.type_adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.Date

class DateMoshiTypeAdapter {
    @FromJson
    fun toLocalDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @ToJson
    fun fromLocalDate(date: Date?): Long? = date?.time
}
