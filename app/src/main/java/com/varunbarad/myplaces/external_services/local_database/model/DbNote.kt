package com.varunbarad.myplaces.external_services.local_database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Notes")
data class DbNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val contents: String,
    @ColumnInfo(name = "timestamp") val timestamp: Date? = null
)
