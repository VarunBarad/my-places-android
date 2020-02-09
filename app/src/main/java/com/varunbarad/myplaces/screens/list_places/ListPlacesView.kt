package com.varunbarad.myplaces.screens.list_places

import com.varunbarad.myplaces.model.UiLocation
import com.varunbarad.myplaces.util.Event
import io.reactivex.Observable

interface ListPlacesView {
    fun onButtonAddPlaceClick(): Observable<Event>

    fun onOpenInMapPlaceClick(): Observable<UiLocation>
    fun onOpenDetailsPlaceClick(): Observable<UiLocation>
    fun onDeletePlaceClick(): Observable<UiLocation>

    fun updateScreen(viewState: ListPlacesViewState)
    fun showMessage(messageText: String)

    fun openAddPlaceScreen()
    fun openPlaceInMap(latitude: Double, longitude: Double)
    fun openPlaceDetailsScreen(placeId: Long)
}
