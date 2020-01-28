package com.varunbarad.myplaces.external_services.local_database

import androidx.room.TypeConverter
import java.util.*

class RoomTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
