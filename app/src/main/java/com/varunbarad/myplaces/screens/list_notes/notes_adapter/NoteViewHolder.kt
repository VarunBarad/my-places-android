package com.varunbarad.myplaces.screens.list_notes.notes_adapter

import androidx.recyclerview.widget.RecyclerView
import com.varunbarad.myplaces.databinding.ListItemNoteBinding
import com.varunbarad.myplaces.model.UiNote

class NoteViewHolder(
    private val viewBinding: ListItemNoteBinding,
    private val noteClickListener: (UiNote) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(note: UiNote) {
        this.viewBinding.textViewTitle.text = note.title
        this.viewBinding.textViewContent.text = note.content

        this.viewBinding.containerNote.setOnClickListener { this.noteClickListener(note) }

        this.viewBinding.executePendingBindings()
    }
}
