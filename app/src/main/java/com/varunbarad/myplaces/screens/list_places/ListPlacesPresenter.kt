package com.varunbarad.myplaces.screens.list_places

import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import com.varunbarad.myplaces.model.UiLocation
import com.varunbarad.myplaces.repositories.PlacesRepository
import com.varunbarad.myplaces.util.toDbLocation
import com.varunbarad.myplaces.util.toUiLocation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ListPlacesPresenter(
    private val view: ListPlacesView,
    private val placesRepository: PlacesRepository
) {
    companion object {
        private const val MESSAGE_PLACE_DELETED = "Place deleted successfully"
    }

    private val serviceDisposables = CompositeDisposable()

    fun onStart() {
        this.serviceDisposables.add(
            this.placesRepository
                .getAllPlacesSortedAlphabeticallyByName()
                .map { databasePlaces: List<DbLocation> -> databasePlaces.map { it.toUiLocation() } }
                .subscribeBy { places ->
                    this.view.updateScreen(
                        ListPlacesViewState(
                            places = places,
                            isNoStoredPlacesMessageVisible = places.isEmpty()
                        )
                    )
                }
        )
    }

    fun addPlace() {
        this.view.openAddPlaceScreen()
    }

    fun openInMap(place: UiLocation) {
        this.view.openPlaceInMap(place.latitude, place.longitude)
    }

    fun openDetails(place: UiLocation) {
        this.view.openPlaceDetailsScreen(place.locationId)
    }

    fun deletePlace(place: UiLocation) {
        this.serviceDisposables.add(
            this.placesRepository
                .deletePlace(place.toDbLocation())
                .subscribeBy { this.view.showMessage(MESSAGE_PLACE_DELETED) }
        )
    }

    fun onStop() {
        this.serviceDisposables.clear()
    }
}
