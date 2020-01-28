package com.varunbarad.myplaces.external_services.local_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface LocationsDao  {
    @Insert
    fun insertLocation(location: DbLocation): Single<Long>

    @Query("select * from Locations order by name")
    fun getAllNotesSortedAlphabeticallyByName(): Observable<List<DbLocation>>

    @Query("select * from Locations order by timestamp desc")
    fun getAllNotesSortedReverseChronologically(): Observable<List<DbLocation>>

    @Query("select * from Locations where id = :locationId")
    fun getLocationDetails(locationId: Long): Single<DbLocation>
}
