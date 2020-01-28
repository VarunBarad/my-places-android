package com.varunbarad.myplaces.screens.list_notes.notes_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.varunbarad.myplaces.databinding.ListItemNoteBinding
import com.varunbarad.myplaces.model.UiNote
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class NotesAdapter : ListAdapter<UiNote, NoteViewHolder>(UiNote.DIFF_CALLBACK) {
    private val noteClickSubject: PublishSubject<UiNote> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            viewBinding = ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            noteClickListener = this::onNoteClickListener
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    private fun onNoteClickListener(note: UiNote) {
        this.noteClickSubject.onNext(note)
    }

    fun getNoteClickObservable(): Observable<UiNote> = this.noteClickSubject
}
