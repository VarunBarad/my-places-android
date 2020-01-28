package com.varunbarad.myplaces

import android.app.Application
import com.varunbarad.myplaces.util.Dependencies

class MyPlacesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // This initializes the notes database
        Dependencies.getNotesDatabase(this)
    }
}
