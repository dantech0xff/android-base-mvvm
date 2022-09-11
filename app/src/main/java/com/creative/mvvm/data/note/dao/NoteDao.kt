package com.creative.mvvm.data.note.dao

import androidx.room.*
import com.creative.mvvm.data.note.entity.Note
import io.reactivex.Single

@Dao
interface NoteDao {

    // insert notes
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Note::class)
    fun insertNotes(notes: Note): Single<Long>

    // update notes
    @Update(onConflict = OnConflictStrategy.REPLACE, entity = Note::class)
    fun updateNotes(notes: Note): Single<Int>

    // get all notes from db
    @Query("SELECT * FROM table_notes ORDER BY date_updated DESC")
    fun getNotes(): Single<List<Note>>

    // delete notes by id
    @Query("DELETE FROM table_notes where id=:id")
    fun deleteNote(id: Int): Single<Int>

    @Query("SELECT * FROM table_notes where id=:id")
    fun getNote(id: Int): Single<Note>

    @Query("UPDATE table_notes SET photo_path=:photoPath WHERE id=:id")
    fun updateNotePhoto(id: Int, photoPath: String): Single<Int>
}
