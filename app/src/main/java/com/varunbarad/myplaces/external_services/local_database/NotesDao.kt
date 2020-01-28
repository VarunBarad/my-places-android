package com.varunbarad.myplaces.external_services.local_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varunbarad.myplaces.external_services.local_database.model.DbNote
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface NotesDao {
    @Insert
    fun insertNote(note: DbNote): Single<Long>

    @Query("select * from Notes order by timestamp desc")
    fun getAllNotesSortedReverseChronologically(): Observable<List<DbNote>>

    @Query("select * from Notes where id = :noteId")
    fun getNoteDetails(noteId: Long): Single<DbNote>
}
