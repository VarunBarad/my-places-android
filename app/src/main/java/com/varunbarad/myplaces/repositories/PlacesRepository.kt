package com.varunbarad.myplaces.repositories

import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface PlacesRepository {
    fun insertNewPlace(
        name: String,
        comments: String,
        latitude: String,
        longitude: String
    ): Single<Long>

    fun getAllPlacesSortedAlphabeticallyByName(): Observable<List<DbLocation>>

    fun getAllPlacesSortedReverseChronologically(): Observable<List<DbLocation>>

    fun getPlaceDetails(locationId: Long): Observable<DbLocation>

    fun updatePlaceDetails(location: DbLocation): Completable

    fun deletePlace(place: DbLocation): Completable
}
