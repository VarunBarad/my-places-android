package com.varunbarad.myplaces.screens.list_places.places_adapter

import com.varunbarad.myplaces.model.UiLocation

interface PlaceClickListener {
    fun onClickOpenInMap(place: UiLocation)
    fun onClickOpenDetails(place: UiLocation)
    fun onClickDelete(place: UiLocation)
}
