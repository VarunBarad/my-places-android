package com.varunbarad.myplaces.screens.list_notes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.myplaces.R
import com.varunbarad.myplaces.databinding.ActivityListNotesBinding
import com.varunbarad.myplaces.model.UiNote
import com.varunbarad.myplaces.screens.add_place.AddPlaceActivity
import com.varunbarad.myplaces.screens.list_notes.notes_adapter.NotesAdapter
import com.varunbarad.myplaces.screens.note_details.NoteDetailsActivity
import com.varunbarad.myplaces.util.Dependencies
import com.varunbarad.myplaces.util.Event
import io.reactivex.Observable

class ListNotesActivity : AppCompatActivity(), ListNotesView {
    private lateinit var viewBinding: ActivityListNotesBinding

    private val notesAdapter: NotesAdapter = NotesAdapter()

    private lateinit var presenter: ListNotesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_notes)
        this.setSupportActionBar(this.viewBinding.toolbar)

        val recyclerViewLayoutManager = LinearLayoutManager(
            this.viewBinding.recyclerViewNotes.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        this.viewBinding.recyclerViewNotes.layoutManager = recyclerViewLayoutManager
        this.viewBinding.recyclerViewNotes.addItemDecoration(
            DividerItemDecoration(
                this.viewBinding.recyclerViewNotes.context,
                recyclerViewLayoutManager.orientation
            )
        )
        this.viewBinding.recyclerViewNotes.adapter = this.notesAdapter

        this.presenter = ListNotesPresenter(
            this,
            Dependencies.getRoomNotesRepository(this)
        )
    }

    override fun onStart() {
        super.onStart()

        this.presenter.onStart()
    }

    override fun onStop() {
        this.presenter.onStop()

        this.viewBinding.buttonCreateNote.setOnClickListener(null)

        super.onStop()
    }

    override fun onButtonCreateNoteClick(): Observable<Event> {
        return Observable.create { emitter ->
            this.viewBinding.buttonCreateNote.setOnClickListener { emitter.onNext(Event.IGNORE) }
            emitter.setCancellable { this.viewBinding.buttonCreateNote.setOnClickListener(null) }
        }
    }

    override fun onNoteClick(): Observable<UiNote> = this.notesAdapter.getNoteClickObservable()

    override fun updateScreen(viewState: ListNotesViewState) {
        this.notesAdapter.submitList(viewState.notes)

        if (viewState.isNoStoredNotesMessageVisible) {
            this.viewBinding.recyclerViewNotes.visibility = View.GONE
            this.viewBinding.textViewEmptyNotesMessage.visibility = View.VISIBLE
        } else {
            this.viewBinding.recyclerViewNotes.visibility = View.VISIBLE
            this.viewBinding.textViewEmptyNotesMessage.visibility = View.GONE
        }
    }

    override fun showMessage(messageText: String) {
        Snackbar.make(
            this.viewBinding.root,
            messageText,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun openCreateNoteScreen() {
        AddPlaceActivity.start(this)
    }

    override fun openNoteDetailsScreen(noteId: Long) {
        NoteDetailsActivity.start(this, noteId)
    }
}
