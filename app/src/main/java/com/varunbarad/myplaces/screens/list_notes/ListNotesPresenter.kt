package com.varunbarad.myplaces.screens.list_notes

import com.varunbarad.myplaces.external_services.local_database.model.DbNote
import com.varunbarad.myplaces.repositories.NotesRepository
import com.varunbarad.myplaces.util.toUiNote
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ListNotesPresenter(
    private val view: ListNotesView,
    private val notesRepository: NotesRepository
) {
    private val serviceDisposables = CompositeDisposable()

    fun onStart() {
        this.serviceDisposables.add(
            this.view
                .onButtonCreateNoteClick()
                .subscribeBy {
                    this.view.openCreateNoteScreen()
                }
        )

        this.serviceDisposables.add(
            this.notesRepository
                .getAllNotesSortedReverseChronologically()
                .map { databaseNotes: List<DbNote> -> databaseNotes.map { it.toUiNote() } }
                .subscribeBy { notes ->
                    this.view.updateScreen(
                        ListNotesViewState(
                            notes = notes,
                            isNoStoredNotesMessageVisible = notes.isEmpty()
                        )
                    )
                }
        )

        this.serviceDisposables.add(
            this.view
                .onNoteClick()
                .subscribeBy { note -> this.view.openNoteDetailsScreen(note.noteId) }
        )
    }

    fun onStop() {
        this.serviceDisposables.clear()
    }
}
