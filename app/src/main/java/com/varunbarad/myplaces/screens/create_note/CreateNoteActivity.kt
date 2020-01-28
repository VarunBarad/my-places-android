package com.varunbarad.myplaces.screens.create_note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.myplaces.R
import com.varunbarad.myplaces.databinding.ActivityCreateNoteBinding
import com.varunbarad.myplaces.screens.note_details.NoteDetailsActivity
import com.varunbarad.myplaces.util.Dependencies
import com.varunbarad.myplaces.util.Event
import io.reactivex.Observable

class CreateNoteActivity : AppCompatActivity(), CreateNoteView {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, CreateNoteActivity::class.java))
        }
    }

    private lateinit var viewBinding: ActivityCreateNoteBinding

    private lateinit var presenter: CreateNotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_create_note
        )

        this.setSupportActionBar(this.viewBinding.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.presenter = CreateNotePresenter(
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

        this.viewBinding.buttonSaveNote.setOnClickListener(null)

        super.onStop()
    }

    override fun onButtonSaveNoteClick(): Observable<Event> {
        return Observable.create { emitter ->
            this.viewBinding.buttonSaveNote.setOnClickListener { emitter.onNext(Event.IGNORE) }
            emitter.setCancellable { this.viewBinding.buttonSaveNote.setOnClickListener(null) }
        }
    }

    override fun getTitleEditTextValue(): String {
        return this.viewBinding.editTextTitle.text.toString()
    }

    override fun getContentsEditTextValue(): String {
        return this.viewBinding.editTextContent.text.toString()
    }

    override fun updateScreen(viewState: CreateNoteViewState) {
        this.viewBinding.editTextTitle.setText(viewState.titleValue)
        this.viewBinding.editTextContent.setText(viewState.contentsValue)

        this.viewBinding.textInputTitle.error = viewState.titleErrorText
        this.viewBinding.textInputContent.error = viewState.contentsErrorText

        this.viewBinding.textInputTitle.isErrorEnabled = viewState.showTitleError
        this.viewBinding.textInputContent.isErrorEnabled = viewState.showContentsError
    }

    override fun showMessage(messageText: String) {
        Snackbar.make(
            this.viewBinding.root,
            messageText,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun openNoteDetailsScreen(noteId: Long) {
        NoteDetailsActivity.start(this, noteId)
        this.finish()
    }
}
