package com.varunbarad.myplaces.screens.list_places

import com.varunbarad.myplaces.model.UiLocation

data class ListPlacesViewState(
    val places: List<UiLocation>,
    val isNoStoredPlacesMessageVisible: Boolean
)
