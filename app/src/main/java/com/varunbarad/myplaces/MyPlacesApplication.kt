package com.varunbarad.myplaces

import android.app.Application
import com.varunbarad.myplaces.util.Dependencies
import net.danlew.android.joda.JodaTimeAndroid

class MyPlacesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Joda Time Android library
        JodaTimeAndroid.init(this)

        // This initializes the notes database
        Dependencies.getNotesDatabase(this)
    }
}
