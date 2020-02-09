package com.varunbarad.myplaces.screens.add_place

import android.location.Location
import com.varunbarad.myplaces.repositories.PlacesRepository
import com.varunbarad.myplaces.util.PermissionRequestResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class AddPlacePresenter(
    private val view: AddPlaceView,
    private val placesRepository: PlacesRepository
) {
    companion object {
        const val ERROR_MESSAGE_NAME_BLANK = "Place cannot have blank name"
        const val ERROR_MESSAGE_COORDINATES_UNAVAILABLE =
            "Place cannot be stored without location coordinates"
        const val ERROR_MESSAGE_SQL_INSERTION = "Error adding place. Try again later."
        const val MESSAGE_LOCATION_PERMISSION_REQUIRED =
            "Location permission is required to get coordinates. Please enable it from settings."
    }

    private val serviceDisposables = CompositeDisposable()

    private var latestLatitude: Double? = null
    private var latestLongitude: Double? = null

    fun onStart() {
        this.serviceDisposables.add(
            this.view
                .onButtonFetchCoordinatesClick()
                .subscribeBy { this.handleFetchCoordinatesButtonClick() }
        )

        this.serviceDisposables.add(
            this.view
                .onButtonSavePlaceClick()
                .subscribeBy { this.handleSaveButtonClick() }
        )
    }

    fun onStop() {
        this.serviceDisposables.clear()
    }

    private fun handleFetchCoordinatesButtonClick() {
        if (this.view.isLocationPermissionAvailable()) {
            this.view.requestLocation(
                locationListener = this::handleLocationUpdate,
                errorListener = this::handleLocationError
            )
        } else {
            this.view.requestLocationPermission()
            this.serviceDisposables.add(
                this.view
                    .permissionRequestResultLocationObservable()
                    .subscribeBy { result: PermissionRequestResult ->
                        when (result) {
                            PermissionRequestResult.GRANTED -> {
                                this.view.requestLocation(
                                    locationListener = this::handleLocationUpdate,
                                    errorListener = this::handleLocationError
                                )
                            }
                            PermissionRequestResult.DENIED -> {
                                this.view.showMessage(MESSAGE_LOCATION_PERMISSION_REQUIRED)
                            }
                        }
                    }
            )
        }
    }

    private fun handleLocationUpdate(location: Location) {
        this.latestLatitude = location.latitude
        this.latestLongitude = location.longitude

        val nameValue = this.view.getNameEditTextValue()
        val commentValue = this.view.getCommentsEditTextValue()
        this.view.updateScreen(
            AddPlaceViewState(
                nameValue = nameValue,
                commentsValue = commentValue,
                nameErrorText = if (nameValue.isBlank()) {
                    ERROR_MESSAGE_NAME_BLANK
                } else {
                    ""
                },
                showNameError = nameValue.isBlank(),
                coordinatesText = "${location.latitude}N, ${location.longitude}E"
            )
        )
    }

    private fun handleLocationError(error: Exception) {
        val message = error.message
        if (message != null) {
            this.view.showMessage(message)
        }
    }

    private fun handleSaveButtonClick() {
        val nameValue = this.view.getNameEditTextValue()
        val commentValue = this.view.getCommentsEditTextValue()
        val latitude: Double? = this.latestLatitude
        val longitude: Double? = this.latestLongitude

        if (nameValue.isBlank()) {
            if ((latitude == null) || (longitude == null)) {
                this.view.updateScreen(
                    AddPlaceViewState(
                        nameValue = nameValue,
                        commentsValue = commentValue,
                        nameErrorText = ERROR_MESSAGE_NAME_BLANK,
                        showNameError = true,
                        coordinatesText = ""
                    )
                )
                this.view.showMessage(ERROR_MESSAGE_COORDINATES_UNAVAILABLE)
            } else {
                this.view.updateScreen(
                    AddPlaceViewState(
                        nameValue = nameValue,
                        commentsValue = commentValue,
                        nameErrorText = ERROR_MESSAGE_NAME_BLANK,
                        showNameError = true,
                        coordinatesText = "${latitude}N, ${longitude}E"
                    )
                )
            }
        } else if ((latitude == null) || (longitude == null)) {
            this.view.updateScreen(
                AddPlaceViewState(
                    nameValue = nameValue,
                    commentsValue = commentValue,
                    nameErrorText = "",
                    showNameError = false,
                    coordinatesText = ""
                )
            )
            this.view.showMessage(ERROR_MESSAGE_COORDINATES_UNAVAILABLE)
        } else {
            this.serviceDisposables.add(
                this.placesRepository
                    .insertNewPlace(
                        name = nameValue,
                        comments = commentValue,
                        latitude = latitude.toString(),
                        longitude = longitude.toString()
                    ).subscribeBy(
                        onError = {
                            // ToDo: Log the error
                            this.view.showMessage(ERROR_MESSAGE_SQL_INSERTION)
                        },
                        onSuccess = { placeId ->
                            this.view.openPlaceDetailsScreen(placeId)
                            this.view.close()
                        }
                    )
            )
        }
    }
}
