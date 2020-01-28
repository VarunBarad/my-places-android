package com.varunbarad.myplaces.util

import android.content.Context
import androidx.room.Room
import com.varunbarad.myplaces.external_services.local_database.MyPlacesDatabase
import com.varunbarad.myplaces.repositories.RoomNotesRepository

object Dependencies {
    private lateinit var notesDatabase: MyPlacesDatabase

    fun getNotesDatabase(context: Context): MyPlacesDatabase {
        synchronized(this) {
            if (!this::notesDatabase.isInitialized) {
                this.notesDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    MyPlacesDatabase::class.java,
                    MyPlacesDatabase.databaseName
                ).build()
            }
        }

        return this.notesDatabase
    }

    fun getRoomNotesRepository(context: Context): RoomNotesRepository {
        return RoomNotesRepository(this.getNotesDatabase(context).notesDao())
    }
}
