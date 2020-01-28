package com.varunbarad.myplaces.screens.create_note

import com.varunbarad.myplaces.external_services.local_database.model.DbNote
import com.varunbarad.myplaces.repositories.NotesRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class CreateNotePresenter(
    private val view: CreateNoteView,
    private val notesRepository: NotesRepository
) {
    companion object {
        const val ERROR_MESSAGE_TITLE_BLANK = "Title cannot be blank"
        const val ERROR_MESSAGE_CONTENT_BLANK = "Content cannot be blank"
        const val ERROR_MESSAGE_TITLE_TOO_LONG = "Title cannot be longer than 100 characters"
    }

    private val serviceDisposables = CompositeDisposable()

    fun onStart() {
        this.serviceDisposables.add(
            this.view
                .onButtonSaveNoteClick()
                .subscribeBy(
                    onError = {}, // This is just placeholder as without this it throws some error
                    onNext = { this.handleSaveButtonClick() }
                )
        )
    }

    fun onStop() {
        this.serviceDisposables.clear()
    }

    private fun handleSaveButtonClick() {
        val titleValue = this.view.getTitleEditTextValue().trim()
        val contentValue = this.view.getContentsEditTextValue().trim()

        if (titleValue.isBlank()) {
            if (contentValue.isBlank()) {
                this.view.updateScreen(
                    CreateNoteViewState(
                        titleValue = titleValue,
                        contentsValue = contentValue,
                        titleErrorText = ERROR_MESSAGE_TITLE_BLANK,
                        contentsErrorText = ERROR_MESSAGE_CONTENT_BLANK,
                        showTitleError = true,
                        showContentsError = true
                    )
                )
            } else {
                this.view.updateScreen(
                    CreateNoteViewState(
                        titleValue = titleValue,
                        contentsValue = contentValue,
                        titleErrorText = ERROR_MESSAGE_TITLE_BLANK,
                        contentsErrorText = "",
                        showTitleError = true,
                        showContentsError = false
                    )
                )
            }
        } else if (titleValue.length > 100) {
            if (contentValue.isBlank()) {
                this.view.updateScreen(
                    CreateNoteViewState(
                        titleValue = titleValue,
                        contentsValue = contentValue,
                        titleErrorText = ERROR_MESSAGE_TITLE_TOO_LONG,
                        contentsErrorText = ERROR_MESSAGE_CONTENT_BLANK,
                        showTitleError = true,
                        showContentsError = true
                    )
                )
            } else {
                this.view.updateScreen(
                    CreateNoteViewState(
                        titleValue = titleValue,
                        contentsValue = contentValue,
                        titleErrorText = ERROR_MESSAGE_TITLE_TOO_LONG,
                        contentsErrorText = "",
                        showTitleError = true,
                        showContentsError = false
                    )
                )
            }
        } else if (contentValue.isBlank()) {
            this.view.updateScreen(
                CreateNoteViewState(
                    titleValue = titleValue,
                    contentsValue = contentValue,
                    titleErrorText = "",
                    contentsErrorText = ERROR_MESSAGE_CONTENT_BLANK,
                    showTitleError = false,
                    showContentsError = true
                )
            )
        } else {
            this.serviceDisposables.add(
                this.notesRepository
                    .insertNewNote(
                        DbNote(
                            title = titleValue,
                            contents = contentValue
                        )
                    ).subscribeBy(
                        onError = {
                            this.view.showMessage("Error adding note. Try again later.")
                        },
                        onSuccess = { noteId ->
                            this.view.openNoteDetailsScreen(noteId)
                        }
                    )
            )
        }
    }
}
