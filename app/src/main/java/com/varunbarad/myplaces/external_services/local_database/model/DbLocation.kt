package com.varunbarad.myplaces.external_services.local_database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Locations")
data class DbLocation(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "comments") val comments: String,
    @ColumnInfo(name = "timestamp") val timestamp: Date
)
