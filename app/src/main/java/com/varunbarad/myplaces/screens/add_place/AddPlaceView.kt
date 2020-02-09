package com.varunbarad.myplaces.screens.add_place

import android.location.Location
import com.varunbarad.myplaces.util.Event
import com.varunbarad.myplaces.util.PermissionRequestResult
import io.reactivex.Observable

interface AddPlaceView {
    fun isLocationPermissionAvailable(): Boolean
    fun requestLocationPermission()
    fun permissionRequestResultLocationObservable(): Observable<PermissionRequestResult>
    fun requestLocation(
        locationListener: (Location) -> Unit,
        errorListener: (Exception) -> Unit
    )

    fun onButtonFetchCoordinatesClick(): Observable<Event>
    fun onButtonSavePlaceClick(): Observable<Event>

    fun getNameEditTextValue(): String
    fun getCommentsEditTextValue(): String

    fun updateScreen(viewState: AddPlaceViewState)
    fun close()

    fun openPlaceDetailsScreen(placeId: Long)

    fun showMessage(messageText: String)
}
