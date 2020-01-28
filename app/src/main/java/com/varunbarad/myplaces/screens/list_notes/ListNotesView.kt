package com.varunbarad.myplaces.screens.list_notes

import com.varunbarad.myplaces.model.UiNote
import com.varunbarad.myplaces.util.Event
import io.reactivex.Observable

interface ListNotesView {
    fun onButtonCreateNoteClick(): Observable<Event>
    fun onNoteClick(): Observable<UiNote>

    fun updateScreen(viewState: ListNotesViewState)
    fun showMessage(messageText: String)

    fun openCreateNoteScreen()
    fun openNoteDetailsScreen(noteId: Long)
}
