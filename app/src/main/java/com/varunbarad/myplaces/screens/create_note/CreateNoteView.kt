package com.varunbarad.myplaces.screens.create_note

import com.varunbarad.myplaces.util.Event
import io.reactivex.Observable

interface CreateNoteView {
    fun onButtonSaveNoteClick(): Observable<Event>

    fun getTitleEditTextValue(): String
    fun getContentsEditTextValue(): String

    fun updateScreen(viewState: CreateNoteViewState)

    fun showMessage(messageText: String)

    fun openNoteDetailsScreen(noteId: Long)
}
