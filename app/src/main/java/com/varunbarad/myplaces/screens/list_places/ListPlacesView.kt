package com.varunbarad.myplaces.screens.list_places

interface ListPlacesView {
    fun updateScreen(viewState: ListPlacesViewState)
    fun showMessage(messageText: String)

    fun openAddPlaceScreen()
    fun openPlaceInMap(latitude: Double, longitude: Double)
    fun openPlaceDetailsScreen(placeId: Long)
    fun openExportFileChooser(fileName: String, mimeType: String)
}
