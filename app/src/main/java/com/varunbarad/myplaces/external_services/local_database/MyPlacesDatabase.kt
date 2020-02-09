package com.varunbarad.myplaces.external_services.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation

@Database(
    entities = [
        DbLocation::class
    ],
    version = MyPlacesDatabase.databaseVersion,
    exportSchema = false // ToDo: Look into Room schema export best-practices
)
@TypeConverters(RoomTypeConverters::class)
abstract class MyPlacesDatabase : RoomDatabase() {
    abstract fun placesDao(): PlacesDao

    companion object {
        const val databaseVersion = 1
        const val databaseName = "MyPlaces-Database"
    }
}
