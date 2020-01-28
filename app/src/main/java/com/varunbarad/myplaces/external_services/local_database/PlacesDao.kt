package com.varunbarad.myplaces.external_services.local_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface PlacesDao  {
    @Insert
    fun insertPlace(location: DbLocation): Single<Long>

    @Query("select * from Locations order by name")
    fun getAllPlacesSortedAlphabeticallyByName(): Observable<List<DbLocation>>

    @Query("select * from Locations order by timestamp desc")
    fun getAllPlacesSortedReverseChronologically(): Observable<List<DbLocation>>

    @Query("select * from Locations where id = :locationId")
    fun getPlaceDetails(locationId: Long): Observable<DbLocation>

    @Update
    fun updatePlaceDetails(location: DbLocation): Completable
}
