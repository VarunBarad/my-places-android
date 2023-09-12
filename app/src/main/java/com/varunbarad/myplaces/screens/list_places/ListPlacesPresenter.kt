package com.varunbarad.myplaces.screens.list_places

import android.util.Log
import com.squareup.moshi.Types
import com.varunbarad.myplaces.external_services.export.model.ExportLocation
import com.varunbarad.myplaces.external_services.local_database.model.DbLocation
import com.varunbarad.myplaces.model.UiLocation
import com.varunbarad.myplaces.repositories.PlacesRepository
import com.varunbarad.myplaces.util.Dependencies
import com.varunbarad.myplaces.util.toDbLocation
import com.varunbarad.myplaces.util.toExportLocation
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
        private const val MESSAGE_IMPORT_ERROR = "There was an error in importing your data"
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

    fun importData() {
        this.view.openImportFileChooser(EXPORT_FILE_MIME_TYPE)
    }

    fun onExportDataFileChooserResult(result: ExportFileChooserResult) {
        when (result) {
            ExportFileChooserResult.Error -> {
                this.view.showMessage(MESSAGE_EXPORT_ERROR)
            }
            is ExportFileChooserResult.Success -> {
                this.serviceDisposables.add(
                    this.placesRepository
                        .getAllPlacesSortedAlphabeticallyByName()
                        .firstOrError()
                        .map { places -> places.map { it.toExportLocation() } }
                        .subscribeBy { places ->
                            try {
                                result.fileOutputStream.use {
                                    val adapter = Dependencies.moshi
                                        .adapter<List<ExportLocation>>(
                                            Types.newParameterizedType(
                                                List::class.java,
                                                ExportLocation::class.java
                                            )
                                        )

                                    it.write(
                                        adapter.toJson(places).toByteArray(
                                            charset = Charsets.UTF_8
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("MyPlaces", e.message, e)
                                this.view.showMessage(MESSAGE_EXPORT_ERROR)
                            }
                        }
                )
            }
        }
    }

    fun onImportDataFileChooserResult(result: ImportFileChooserResult) {
        when (result) {
            ImportFileChooserResult.Error -> {
                this.view.showMessage(MESSAGE_IMPORT_ERROR)
            }
            is ImportFileChooserResult.Success -> {
                try {
                    val adapter = Dependencies.moshi
                        .adapter<List<ExportLocation>>(
                            Types.newParameterizedType(
                                List::class.java,
                                ExportLocation::class.java,
                            ),
                        )
                    val jsonString = result.fileInputStream.bufferedReader(charset = Charsets.UTF_8).use { it.readText() }
                    val placesToImport: List<ExportLocation> = adapter.fromJson(jsonString)!!
                    val dbLocations = placesToImport.map { it.toDbLocation().copy(id = null) }
                    this.serviceDisposables.add(
                        this.placesRepository.insertAllPlaces(places = dbLocations)
                            .subscribeBy(
                                onError = {
                                    Log.e("MyPlaces", it.message, it)
                                    this.view.showMessage(MESSAGE_IMPORT_ERROR)
                                },
                                onSuccess = { placeIds ->
                                    val successMessage = when (val numberOfLocationsImported = placeIds.size) {
                                        1 -> "Successfully imported $numberOfLocationsImported location"
                                        else -> "Successfully imported $numberOfLocationsImported locations"
                                    }
                                    this.view.showMessage(successMessage)
                                },
                            )
                    )
                } catch (e: Exception) {
                    Log.e("MyPlaces", e.message, e)
                    this.view.showMessage(MESSAGE_IMPORT_ERROR)
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
