package com.varunbarad.myplaces.screens.note_details

interface NoteDetailsView {
    fun updateScreen(viewState: NoteDetailsViewState)

    fun showMessage(messageText: String)
}
