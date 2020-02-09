package com.varunbarad.myplaces.screens.place_details

data class PlaceDetailsViewState(
    val placeNameText: String,
    val placeTimestampText: String,
    val placeCommentText: String,
    val placeCoordinatesText: String,
    val isLoaderVisible: Boolean
)
