package com.varunbarad.myplaces.external_services.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import com.varunbarad.myplaces.external_services.local_database.model.DbNote

@Database(
    entities = [
        DbNote::class,
        DbLocation::class
    ],
    version = MyPlacesDatabase.databaseVersion
)
@TypeConverters(RoomTypeConverters::class)
abstract class MyPlacesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun locationsDao(): LocationsDao

    companion object {
        const val databaseVersion = 1
        const val databaseName = "MyPlaces-Database"
    }
}
