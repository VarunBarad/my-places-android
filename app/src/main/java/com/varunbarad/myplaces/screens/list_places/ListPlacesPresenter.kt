package com.varunbarad.myplaces.screens.list_places

import android.util.Log
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import com.varunbarad.myplaces.model.UiLocation
import com.varunbarad.myplaces.repositories.PlacesRepository
import com.varunbarad.myplaces.util.toDbLocation
import com.varunbarad.myplaces.util.toUiLocation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import org.joda.time.LocalDateTime

class ListPlacesPresenter(
    private val view: ListPlacesView,
    private val placesRepository: PlacesRepository
) {
    companion object {
        private const val MESSAGE_PLACE_DELETED = "Place deleted successfully"
        private const val MESSAGE_EXPORT_ERROR = "There was an error in exporting your data"
        private const val EXPORT_FILE_MIME_TYPE = "application/json"
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

    fun exportData() {
        this.view.openExportFileChooser(exportFileName(), EXPORT_FILE_MIME_TYPE)
    }

    fun onExportDataFileChooserResult(result: FileChooserResult) {
        when (result) {
            FileChooserResult.Error -> {
                this.view.showMessage(MESSAGE_EXPORT_ERROR)
            }
            is FileChooserResult.Success -> {
                try {
                    result.fileOutputStream.use {
                        // ToDo: Write the JSON of all the objects
                        it.write("{\"name\":\"Varun\",\"message\":\"Finally\"}".toByteArray(Charsets.UTF_8))
                    }
                } catch (e: Exception) {
                    Log.e("MyPlaces", e.message, e)
                    this.view.showMessage(MESSAGE_EXPORT_ERROR)
                }
            }
        }
    }

    private fun exportFileName(): String {
        val date = LocalDateTime.now().toString("YYYY-MM-dd")
        return "my-places-${date}.json"
    }

    fun onStop() {
        this.serviceDisposables.clear()
    }
}
