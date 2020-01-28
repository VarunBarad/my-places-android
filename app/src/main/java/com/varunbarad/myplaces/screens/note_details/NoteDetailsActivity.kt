package com.varunbarad.myplaces.screens.note_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.myplaces.R
import com.varunbarad.myplaces.databinding.ActivityNoteDetailsBinding
import com.varunbarad.myplaces.util.Dependencies

class NoteDetailsActivity : AppCompatActivity(), NoteDetailsView {
    companion object {
        private const val EXTRA_NOTE_ID = "note-id"

        @JvmStatic
        fun start(context: Context, noteId: Long) {
            context.startActivity(Intent(context, NoteDetailsActivity::class.java).apply {
                putExtra(EXTRA_NOTE_ID, noteId)
            })
        }
    }

    private lateinit var viewBinding: ActivityNoteDetailsBinding

    private lateinit var presenter: NoteDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_note_details
        )

        this.setSupportActionBar(this.viewBinding.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.presenter = NoteDetailsPresenter(
            this,
            Dependencies.getRoomNotesRepository(this),
            this.getPassedNoteId()
        )
    }

    override fun onStart() {
        super.onStart()

        this.presenter.onStart()
    }

    override fun onStop() {
        this.presenter.onStop()

        super.onStop()
    }

    private fun getPassedNoteId(): Long {
        val passedNoteId = this.intent.extras?.getLong(EXTRA_NOTE_ID)

        if (passedNoteId == null) {
            throw IllegalArgumentException("No note-id was passed to note-details screen")
        } else {
            return passedNoteId
        }
    }

    override fun updateScreen(viewState: NoteDetailsViewState) {
        this.viewBinding.textViewTitle.text = viewState.noteTitleText
        this.viewBinding.textViewTimestamp.text = viewState.noteTimestampText
        this.viewBinding.textViewContent.text = viewState.noteContentText

        if (viewState.isLoaderVisible) {
            this.viewBinding.containerNoteDetails.visibility = View.GONE
            this.viewBinding.progressBar.visibility = View.VISIBLE
        } else {
            this.viewBinding.containerNoteDetails.visibility = View.VISIBLE
            this.viewBinding.progressBar.visibility = View.GONE
        }
    }

    override fun showMessage(messageText: String) {
        Snackbar.make(
            this.viewBinding.root,
            messageText,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
