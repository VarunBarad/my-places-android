package com.varunbarad.myplaces.repositories

import com.varunbarad.myplaces.external_services.local_database.NotesDao
import com.varunbarad.myplaces.external_services.local_database.model.DbNote
import com.varunbarad.myplaces.util.ThreadSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class RoomNotesRepository(private val notesDao: NotesDao) : NotesRepository {
    override fun insertNewNote(note: DbNote): Single<Long> {
        return notesDao.insertNote(
            note.copy(timestamp = Date(System.currentTimeMillis()))
        ).subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }

    override fun getAllNotesSortedReverseChronologically(): Observable<List<DbNote>> {
        return notesDao.getAllNotesSortedReverseChronologically()
            .subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }

    override fun getNoteDetails(noteId: Long): Single<DbNote> {
        return notesDao.getNoteDetails(noteId)
            .subscribeOn(ThreadSchedulers.io())
            .observeOn(ThreadSchedulers.main())
    }
}
