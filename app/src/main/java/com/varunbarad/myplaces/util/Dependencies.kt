package com.varunbarad.myplaces.util

import android.content.Context
import androidx.room.Room
import com.varunbarad.myplaces.external_services.local_database.MyPlacesDatabase
import com.varunbarad.myplaces.repositories.RoomPlacesRepository

object Dependencies {
    private lateinit var placesDatabase: MyPlacesDatabase

    fun getPlacesDatabase(context: Context): MyPlacesDatabase {
        synchronized(this) {
            if (!this::placesDatabase.isInitialized) {
                this.placesDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    MyPlacesDatabase::class.java,
                    MyPlacesDatabase.databaseName
                ).build()
            }
        }

        return this.placesDatabase
    }

    fun getRoomPlacesRepository(context: Context): RoomPlacesRepository {
        return RoomPlacesRepository(this.getPlacesDatabase(context).placesDao())
    }
}
