package com.varunbarad.myplaces.screens.add_place

data class AddPlaceViewState(
    val nameValue: String,
    val commentsValue: String,
    val nameErrorText: String,
    val showNameError: Boolean,
    val coordinatesText: String,
    val showLocationPermissionExplanationText: Boolean
)
