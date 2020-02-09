package com.varunbarad.myplaces.screens.place_details

import com.varunbarad.myplaces.repositories.PlacesRepository
import com.varunbarad.myplaces.util.toDbLocation
import com.varunbarad.myplaces.util.toUiLocation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.text.SimpleDateFormat
import java.util.*

class PlaceDetailsPresenter(
    private val view: PlaceDetailsView,
    private val placesRepository: PlacesRepository,
    private val placeId: Long
) {
    companion object {
        private const val ERROR_MESSAGE_CANNOT_LOAD_PLACE = "Error loading place. Try again later."
        private const val ERROR_MESSAGE_CANNOT_DELETE_PLACE =
            "Error deleting place. Try again later."

        private const val SUCCESS_MESSAGE_DELETE_PLACE = "Place deleted successfully."
        private const val MESSAGE_COORDINATES_COPIED = "Coordinates copied"
    }

    private val timestampFormat = SimpleDateFormat("dd LLL yyyy, hh:mm a", Locale.getDefault())

    private val serviceDisposables = CompositeDisposable()

    fun onStart() {
        this.serviceDisposables.add(
            this.placesRepository
                .getPlaceDetails(placeId)
                .map { dbPlace -> dbPlace.toUiLocation() }
                .subscribeBy(
                    onError = { error ->
                        this.view.updateScreen(
                            PlaceDetailsViewState(
                                placeNameText = "",
                                placeTimestampText = "",
                                placeCommentText = "",
                                placeCoordinatesText = "",
                                isLoaderVisible = false
                            )
                        )
                        // ToDo: Log error
                        this.view.showMessage(ERROR_MESSAGE_CANNOT_LOAD_PLACE)
                    },
                    onNext = { place ->
                        this.view.updateScreen(
                            PlaceDetailsViewState(
                                placeNameText = place.name,
                                placeTimestampText = timestampFormat.format(place.timestamp),
                                placeCommentText = place.comments,
                                placeCoordinatesText = "${place.latitude}N, ${place.longitude}E",
                                isLoaderVisible = false
                            )
                        )

                        this.serviceDisposables.add(
                            this.view
                                .onButtonCopyCoordinatesClick()
                                .subscribeBy {
                                    this.view.copyTextToClipboard(
                                        "${place.latitude}N, ${place.longitude}E"
                                    )
                                    this.view.showMessage(MESSAGE_COORDINATES_COPIED)
                                }
                        )

                        this.serviceDisposables.add(
                            this.view
                                .onButtonOpenInMapPlaceClick()
                                .subscribeBy {
                                    this.view.openPlaceInMap(
                                        place.latitude,
                                        place.longitude
                                    )
                                }
                        )

                        this.serviceDisposables.add(
                            this.view
                                .onButtonDeletePlaceClick()
                                .subscribeBy {
                                    this.placesRepository
                                        .deletePlace(place.toDbLocation())
                                        .subscribeBy(
                                            onError = {
                                                this.view.showMessage(
                                                    ERROR_MESSAGE_CANNOT_DELETE_PLACE
                                                )
                                            },
                                            onComplete = {
                                                this.view.showTemporaryMessage(
                                                    SUCCESS_MESSAGE_DELETE_PLACE
                                                )
                                            }
                                        )
                                }
                        )
                    }
                )
        )

        this.view.updateScreen(
            PlaceDetailsViewState(
                placeNameText = "",
                placeTimestampText = "",
                placeCommentText = "",
                placeCoordinatesText = "",
                isLoaderVisible = true
            )
        )
    }

    fun onStop() {
        this.serviceDisposables.clear()
    }
}
