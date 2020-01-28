package com.varunbarad.myplaces.screens.list_notes

import com.varunbarad.myplaces.model.UiNote

data class ListNotesViewState(
    val notes: List<UiNote>,
    val isNoStoredNotesMessageVisible: Boolean
)
