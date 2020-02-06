package com.varunbarad.myplaces.repositories

import com.varunbarad.myplaces.external_services.local_database.PlacesDao
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import com.varunbarad.myplaces.util.ThreadSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class RoomPlacesRepository(private val placesDao: PlacesDao) : PlacesRepository {
    override fun insertNewPlace(
        name: String,
        comments: String,
        latitude: String,
        longitude: String
    ): Single<Long> {
        return placesDao.insertPlace(
            DbLocation(
                name = name,
                comments = comments,
                timestamp = Date(System.currentTimeMillis()),
                latitude = latitude,
                longitude = longitude
            )
        ).subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }

    override fun getAllPlacesSortedAlphabeticallyByName(): Observable<List<DbLocation>> {
        return placesDao.getAllPlacesSortedAlphabeticallyByName()
            .subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }

    override fun getAllPlacesSortedReverseChronologically(): Observable<List<DbLocation>> {
        return placesDao.getAllPlacesSortedReverseChronologically()
            .subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }

    override fun getPlaceDetails(locationId: Long): Observable<DbLocation> {
        return placesDao.getPlaceDetails(locationId)
            .subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }

    override fun updatePlaceDetails(location: DbLocation): Completable {
        return placesDao.updatePlaceDetails(location)
            .subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }
}
