package com.varunbarad.myplaces.screens.place_details

import com.varunbarad.myplaces.util.Event
import io.reactivex.Observable

interface PlaceDetailsView {
    fun onButtonCopyCoordinatesClick(): Observable<Event>
    fun onButtonDeletePlaceClick(): Observable<Event>
    fun onButtonOpenInMapPlaceClick(): Observable<Event>

    fun updateScreen(viewState: PlaceDetailsViewState)
    fun showMessage(messageText: String)
    fun showTemporaryMessage(messageText: String)
    fun close()

    fun copyTextToClipboard(textContents: String)
    fun openPlaceInMap(latitude: Double, longitude: Double)
}
