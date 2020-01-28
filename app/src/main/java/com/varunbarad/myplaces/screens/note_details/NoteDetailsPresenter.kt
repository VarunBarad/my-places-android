package com.varunbarad.myplaces.screens.note_details

import android.util.Log
import com.varunbarad.myplaces.repositories.NotesRepository
import com.varunbarad.myplaces.util.toUiNote
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailsPresenter(
    private val view: NoteDetailsView,
    private val notesRepository: NotesRepository,
    private val noteId: Long
) {
    private val timestampFormat = SimpleDateFormat("dd LLL yyyy, hh:mm a", Locale.getDefault())

    private val serviceDisposables = CompositeDisposable()

    fun onStart() {
        this.serviceDisposables.add(
            this.notesRepository
                .getNoteDetails(noteId)
                .map { dbNote -> dbNote.toUiNote() }
                .subscribeBy(
                    onError = { error ->
                        this.view.updateScreen(
                            NoteDetailsViewState(
                                noteTitleText = "",
                                noteContentText = "",
                                noteTimestampText = "",
                                isLoaderVisible = false
                            )
                        )
                        Log.e(
                            NoteDetailsPresenter::class.java.canonicalName,
                            error.message ?: "Error message was null"
                        )
                        this.view.showMessage("Error loading note. Try again later.")
                    },
                    onSuccess = { note ->
                        this.view.updateScreen(
                            NoteDetailsViewState(
                                noteTitleText = note.title,
                                noteContentText = note.content,
                                noteTimestampText = this.timestampFormat.format(note.timestamp),
                                isLoaderVisible = false
                            )
                        )
                    }
                )
        )

        this.view.updateScreen(
            NoteDetailsViewState(
                noteTitleText = "",
                noteContentText = "",
                noteTimestampText = "",
                isLoaderVisible = true
            )
        )
    }

    fun onStop() {
        this.serviceDisposables.clear()
    }
}
